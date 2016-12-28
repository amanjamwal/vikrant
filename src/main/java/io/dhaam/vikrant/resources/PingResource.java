package io.dhaam.vikrant.resources;

import com.google.inject.persist.Transactional;

import com.codahale.metrics.annotation.Timed;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.dhaam.common.authentication.Secured;
import io.dhaam.vikrant.dao.CacheDAO;
import io.dhaam.vikrant.entity.Tuple;

/**
 * @author ajamwal
 * @since 12/13/16
 */

@Path("/ping")
@Produces(MediaType.APPLICATION_JSON)
@Transactional
@Secured
public class PingResource {

  private final CacheDAO cacheDAO;

  @Inject
  public PingResource(CacheDAO cacheDAO) {
    this.cacheDAO = cacheDAO;
  }

  @GET
  @Timed
  public String ping(@QueryParam("key") String key) {
    Optional<Tuple> tupleOptional = this.cacheDAO.findOne(key);
    if (tupleOptional.isPresent()) {
      return tupleOptional.get().getItem();
    } else {
      return "dummy";
    }
  }

  @Path("item")
  @GET
  @Timed
  public String pingItem(@QueryParam("item") String item) {
    Optional<Tuple> tupleOptional = this.cacheDAO.findKey(item);
    if (tupleOptional.isPresent()) {
      return tupleOptional.get().getItem();
    } else {
      return "dummy";
    }
  }

  @POST
  @Timed
  public String ping(@QueryParam("key") String key, @QueryParam("item") String item) {
    cacheDAO.persist(new Tuple(key, item));
    return key;
  }
}
