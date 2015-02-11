// @formatter:off
package com.everhomes.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.everhomes.bus.LocalBusMessageClassRegistry;

@Component
public class BorderServerBootstrapBean implements ApplicationListener<ApplicationEvent> {

    @Autowired
    private LocalBusMessageClassRegistry messageClassRegistry;
    
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof ContextRefreshedEvent) {
            if(messageClassRegistry.getPackages().isEmpty()) {
                messageClassRegistry.addPackage("com.everhomes");
                messageClassRegistry.scan(this.getClass().getClassLoader());
            }
        }
    }
}
