// @formatter:off
package com.everhomes.core;

import com.everhomes.atomikos.AtomikosHelper;
import com.everhomes.bus.LocalEventBus;
import com.everhomes.sequence.MysqlSequenceProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

//import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;

/**
 * Spring boot main class. Its class name need to be packaged to let Spring boot bootstrap processStat
 * be aware of.
 * 
 * Maven spring boot plugin can take its name from maven property(start-class)
 *  
 * @author Kelven Yang
 *
 */
@ComponentScan(value={"com.everhomes"}, excludeFilters={
        @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value={MysqlSequenceProviderImpl.class})})
@EnableAutoConfiguration(exclude={
    XADataSourceAutoConfiguration.class,
    DataSourceAutoConfiguration.class, 
    HibernateJpaAutoConfiguration.class,
    FreeMarkerAutoConfiguration.class
})
@EnableBinding(LocalEventBus.BusEventChannel.class)
public class CoreServerApp {
    final static Logger logger = LoggerFactory.getLogger(CoreServerApp.class);
    
    public static void main(String[] args) {
        AtomikosHelper.fixup();
        
        try {
            SpringApplication app = new SpringApplication(CoreServerApp.class);
            // app.setShowBanner(false);
            app.setWebEnvironment(true);
            app.run(args);
        } catch(Throwable e) {
            logger.error("Startup exception", e);
        }
    }
}
