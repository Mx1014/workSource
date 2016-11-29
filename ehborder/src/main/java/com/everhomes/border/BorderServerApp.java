// @formatter:off
package com.everhomes.border;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

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
    DataSourceAutoConfiguration.class, 
    HibernateJpaAutoConfiguration.class
})
public class BorderServerApp {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BorderServerApp.class);
        // app.setShowBanner(false);
        app.run(args);
    }
}
