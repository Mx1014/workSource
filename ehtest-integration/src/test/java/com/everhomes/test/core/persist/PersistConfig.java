package com.everhomes.test.core.persist;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.MappedSchema;
import org.jooq.conf.RenderMapping;
import org.jooq.conf.Settings;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
@ComponentScan(basePackages = {"com.everhomes.test"})
@PropertySource("classpath:config/ehcore.properties")
public class PersistConfig {
    @Autowired
    private Environment env;
 
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
    	BasicDataSource dataSource = new BasicDataSource();
 
        dataSource.setDriverClassName(env.getRequiredProperty("db.driver"));
        dataSource.setUrl(env.getRequiredProperty("db.url"));
        dataSource.setUsername(env.getRequiredProperty("db.username"));
        dataSource.setPassword(env.getRequiredProperty("db.password"));
 
        return dataSource;
    }
 
    @Bean
    public LazyConnectionDataSourceProxy lazyConnectionDataSource() {
        return new LazyConnectionDataSourceProxy(dataSource());
    }
 
    @Bean
    public TransactionAwareDataSourceProxy transactionAwareDataSource() {
        return new TransactionAwareDataSourceProxy(lazyConnectionDataSource());
    }
 
    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(lazyConnectionDataSource());
    }
 
    @Bean
    public DataSourceConnectionProvider connectionProvider() {
        return new DataSourceConnectionProvider(transactionAwareDataSource());
    }
 
    @Bean
    public JOOQToSpringExceptionTransformer jooqToSpringExceptionTransformer() {
        return new JOOQToSpringExceptionTransformer();
    }
 
    @Bean
    public DefaultConfiguration configuration() {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
 
        jooqConfiguration.set(connectionProvider());
        jooqConfiguration.set(new DefaultExecuteListenerProvider(
            jooqToSpringExceptionTransformer()
        ));
 
        String sqlDialectName = env.getRequiredProperty("jooq.sql.dialect");
        SQLDialect dialect = SQLDialect.valueOf(sqlDialectName);
        jooqConfiguration.set(dialect);
        
        String dbName = env.getProperty("db.name");
        String mapptingDbName = env.getProperty("db.mapping_name");
        if(dbName != null && dbName.trim().length() > 0 && mapptingDbName != null && mapptingDbName.trim().length() > 0)  {
            RenderMapping mapping = new RenderMapping().withSchemata(new MappedSchema().withInput(dbName).withOutput(mapptingDbName));
            Settings settings = new Settings().withRenderMapping(mapping);
            jooqConfiguration.set(settings);
        }
 
        return jooqConfiguration;
    }
 
    @Bean
    public DSLContext dsl() {
        return new DefaultDSLContext(configuration());
    }
 
//    @Bean
//    public DataSourceInitializer dataSourceInitializer() {
//        DataSourceInitializer initializer = new DataSourceInitializer();
//        initializer.setDataSource(dataSource());
// 
//        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
//        populator.addScript(new ClassPathResource(env.getRequiredProperty("db.schema.script")));
// 
//        initializer.setDatabasePopulator(populator);
//        return initializer;
//    }
}
