package io.dhaam.common.jpa;

import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Collection;
import java.util.Properties;
import java.util.Set;

import javax.inject.Provider;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import io.dhaam.common.jpa.internal.DatabaseModule;
import io.dhaam.common.jpa.transaction.CustomAnnotationTransactionAttributeSource;
import io.dhaam.common.jpa.transaction.PlatformTransactionManagerProxy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

/**
 * @author ajamwal
 * @since 12/13/16
 */

@Getter
@Slf4j
public class JpaPersistenceModule extends AbstractModule {

  public static final String PHYSICAL_NAMING_STRATEGY = "hibernate.physical_naming_strategy";
  private final Set<String> packagesToScan;
  private Properties jpaProperties;
  private String persistUnitName = "dhaam";

  public JpaPersistenceModule(Set<String> packagesToScan) {
    this.packagesToScan = packagesToScan;
    this.jpaProperties = new Properties();
    this.jpaProperties.put(PHYSICAL_NAMING_STRATEGY, ImprovedNamingStrategy.INSTANCE);
  }

  public JpaPersistenceModule properties(Properties properties) {
    this.jpaProperties = properties;
    return this;
  }

  @Override
  protected void configure() {
    install(new DatabaseModule());
    bindTransactionalInterceptor();
  }

  @Provides
  @Singleton
  private PlatformTransactionManager providesPlatformTransactionManager(
      Provider<EntityManagerFactory> entityManagerFactoryProvider) {

    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactoryProvider.get());
    transactionManager.afterPropertiesSet();
    return transactionManager;
  }

  @Provides
  @Singleton
  private TransactionTemplate providesTransactionTemplate(
      Provider<PlatformTransactionManager> platformTransactionManagerProvider) {

    TransactionTemplate transactionTemplate = new TransactionTemplate(
        platformTransactionManagerProvider.get()
    );

    transactionTemplate.afterPropertiesSet();
    return transactionTemplate;
  }

  @Provides
  @Singleton
  private EntityManagerFactory providesEntityManagerFactory(
      Provider<DataSource> dataSourceProvider) {

    log.info("Creating entity manager factory");

    LocalContainerEntityManagerFactoryBean
        entityManagerFactoryBean =
        new LocalContainerEntityManagerFactoryBean();
    entityManagerFactoryBean.setDataSource(dataSourceProvider.get());
    entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    entityManagerFactoryBean
        .setPackagesToScan(packagesToScan.toArray(new String[packagesToScan.size()]));

    Collection<String> mappingFiles = findMappingFilesIn(packagesToScan);
    entityManagerFactoryBean
        .setMappingResources(mappingFiles.toArray(new String[mappingFiles.size()]));

    entityManagerFactoryBean.setJpaProperties(jpaProperties);
    entityManagerFactoryBean.afterPropertiesSet();

    log.info("Successfully created entity manager factory");
    return entityManagerFactoryBean.getObject();
  }

  @Provides
  private EntityManager providesEntityManager(Provider<EntityManagerFactory> emFactoryProvider) {

    if (TransactionSynchronizationManager.hasResource(emFactoryProvider.get())) {
      EntityManagerHolder entityManagerHolder =
          (EntityManagerHolder) TransactionSynchronizationManager.getResource(
              emFactoryProvider.get()
          );
      return entityManagerHolder.getEntityManager();
    }
    throw new IllegalStateException("No thread bound entity manager found");
  }

  private void bindTransactionalInterceptor() {
    PlatformTransactionManagerProxy
        platformTransactionManagerProxy =
        new PlatformTransactionManagerProxy();

    bind(PlatformTransactionManagerProxy.class).toInstance(platformTransactionManagerProxy);

    TransactionInterceptor transactionInterceptor = new TransactionInterceptor(
        platformTransactionManagerProxy,
        new CustomAnnotationTransactionAttributeSource(false)
    );

    bindInterceptor(any(), annotatedWith(Transactional.class), transactionInterceptor);
    bindInterceptor(annotatedWith(Transactional.class), any(), transactionInterceptor);

    bindInterceptor(
        any(),
        annotatedWith(com.google.inject.persist.Transactional.class),
        transactionInterceptor);

    bindInterceptor(
        annotatedWith(com.google.inject.persist.Transactional.class),
        any(),
        transactionInterceptor);
  }

  private Collection<String> findMappingFilesIn(Set<String> packagesToScan) {
    log.info("Finding mapping files.");

    Set<String> mappingFiles = Sets.newHashSet();
    packagesToScan.forEach(packageToScan ->
        mappingFiles.addAll(getMappingFilesInPackage(packageToScan))
    );

    log.debug("Found mapping files: {}.", mappingFiles);
    return mappingFiles;
  }

  private Collection<String> getMappingFilesInPackage(String packageToScan) {
    log.info("Scanning mapping files in {}.", packageToScan);
    Reflections reflections = new Reflections(packageToScan, new ResourcesScanner());
    return reflections.getResources(input -> input != null && input.endsWith(".hbm.xml"));
  }
}