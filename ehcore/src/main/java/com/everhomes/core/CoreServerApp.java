// @formatter:off
package com.everhomes.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import com.everhomes.atomikos.AtomikosHelper;

/**
 * Spring boot main class. Its class name need to be packaged to let Spring boot bootstrap process
 * be aware of.
 * 
 * Maven spring boot plugin can take its name from maven property(start-class)
 *  
 * @author Kelven Yang
 *
 */
@ComponentScan(value={"com.everhomes"})
@EnableAutoConfiguration(exclude={
//    XADataSourceAutoConfiguration.class,
    DataSourceAutoConfiguration.class, 
    HibernateJpaAutoConfiguration.class,
    FreeMarkerAutoConfiguration.class
})
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
