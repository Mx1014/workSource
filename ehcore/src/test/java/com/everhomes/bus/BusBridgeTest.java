package com.everhomes.bus;


import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.everhomes.junit.PropertyInitializer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = { PropertyInitializer.class },
    loader = AnnotationConfigContextLoader.class)
public class BusBridgeTest  extends TestCase {

    @Autowired
    private LocalBusProvider localBusProvider;
    
    @Configuration
    @ComponentScan(basePackages = {
            "com.everhomes"
        })
    @EnableAutoConfiguration(exclude={
            DataSourceAutoConfiguration.class, 
            HibernateJpaAutoConfiguration.class,
        })
    static class ContextConfiguration {
        @Bean
        public TaskScheduler taskScheduler () {
            ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
            scheduler.setPoolSize(10);
            return scheduler;
        }
    }
    
    @Test
    public void testBridgeForwarding() {
        this.localBusProvider.publish(null, "global.bus.message", "Hello, world");
        
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
