package com.everhomes.junit;

import com.everhomes.sequence.MysqlSequenceProviderImpl;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(
        initializers = {PropertyInitializer.class},
        classes = {CoreServerTestCase.ContextConfiguration.class}
)
public class CoreServerTestCase extends TestCase {
    @ComponentScan(basePackages = {
            "com.everhomes"
    }, excludeFilters = {
            @ComponentScan.Filter(
                    type = FilterType.ASSIGNABLE_TYPE,
                    classes = {MysqlSequenceProviderImpl.class}
            )
    })
    // @EnableAutoConfiguration(exclude = {
    //         DataSourceAutoConfiguration.class,
    //         XADataSourceAutoConfiguration.class,
    //         HibernateJpaAutoConfiguration.class,
    //         FreeMarkerAutoConfiguration.class
    // })
    @ImportResource(locations = "classpath:applicationContext.xml")
    static class ContextConfiguration {
        // @Bean
        // public TaskScheduler taskScheduler () {
        //     ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        //     scheduler.setPoolSize(10);
        //     return scheduler;
        // }
    }
}
