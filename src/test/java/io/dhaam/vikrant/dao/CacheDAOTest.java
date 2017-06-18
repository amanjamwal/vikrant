package io.dhaam.vikrant.dao;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import io.dhaam.common.jpa.AbstractDAOTest;
import io.dhaam.common.jpa.TestModule;
import io.dhaam.vikrant.entity.Tuple;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

/**
 * @author ajamwal
 * @since 12/29/16
 */

@Guice(modules = TestModule.class)
public class CacheDAOTest extends AbstractDAOTest {

  private final CacheDAO cacheDAO;

  @Inject
  public CacheDAOTest(CacheDAO cacheDAO) {
    this.cacheDAO = cacheDAO;
  }

  @BeforeMethod
  public void setUp() throws Exception {
    initMocks(this);
  }

  @Test
  public void testFindKey() throws Exception {
    Tuple testTuple = new Tuple("key", "value");
    cacheDAO.persist(testTuple);
    assertEquals(testTuple, cacheDAO.findKey("value").orElse(null));
  }

}