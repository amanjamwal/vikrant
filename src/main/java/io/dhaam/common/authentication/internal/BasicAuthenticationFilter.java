package io.dhaam.common.authentication.internal;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import io.dhaam.common.ErrorResponse;
import io.dhaam.common.authentication.AuthenticationDAO;
import io.dhaam.common.authentication.Secured;

/**
 * @author ajamwal
 * @since 12/21/16
 */

@Secured
@Provider
public class BasicAuthenticationFilter implements ContainerRequestFilter {
  private static final String HEADER_API_KEY = "X-API-KEY";

  private final AuthenticationDAO authenticationDAO;

  @Inject
  public BasicAuthenticationFilter(AuthenticationDAO authenticationDAO) {
    this.authenticationDAO = authenticationDAO;
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    final Optional<String> oApiKey = headerParam(requestContext, HEADER_API_KEY);
    if (!oApiKey.isPresent()) {
      requestContext.abortWith(missingParameter(HEADER_API_KEY));
      return;
    }

    if (oApiKey.isPresent()) {
      BasicAuthRequest authRequest = new BasicAuthRequest(oApiKey.get());
      if (!authenticationDAO.authenticate(authRequest)) {
        requestContext.abortWith(unauthorized());
      }
    }
  }

  private Optional<String> headerParam(ContainerRequestContext requestContext, String param) {
    final MultivaluedMap<String, String> headerMap = requestContext.getHeaders();
    final List<String> values = headerMap.get(param);
    return Optional.ofNullable(values == null || values.isEmpty() ? null : values.get(0));
  }

  private Response missingParameter(String name) {
    return Response.status(Status.BAD_REQUEST)
        .type(MediaType.APPLICATION_JSON)
        .entity(
            ErrorResponse.builder()
                .code(Status.BAD_REQUEST.getStatusCode())
                .description("Parameter '" + name + "' is required.")
                .build()
        )
        .build();
  }

  private Response unauthorized() {
    return Response.status(Response.Status.UNAUTHORIZED)
        .type(MediaType.APPLICATION_JSON)
        .entity(
            ErrorResponse.builder()
                .code(Status.UNAUTHORIZED.getStatusCode())
                .description("Unauthorized")
                .build()
        )
        .build();
  }
}
