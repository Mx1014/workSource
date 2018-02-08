package com.everhomes.junit;

import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = {PropertyInitializer.class},
        loader = AnnotationConfigContextLoader.class)
public class CoreServerTestCase extends TestCase {

    @Configuration
    @ComponentScan(basePackages = {
            "com.everhomes"
    })
    @EnableAutoConfiguration(exclude = {
            DataSourceAutoConfiguration.class,
            HibernateJpaAutoConfiguration.class,
            FreeMarkerAutoConfiguration.class
    })
    static class ContextConfiguration {
        // @Bean
        // public TaskScheduler taskScheduler() {
        //     ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        //     scheduler.setPoolSize(10);
        //     return scheduler;
        // }
    }
}
