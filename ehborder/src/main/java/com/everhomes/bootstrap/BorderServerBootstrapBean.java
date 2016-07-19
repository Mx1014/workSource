// @formatter:off
package com.everhomes.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.everhomes.bus.LocalBusMessageClassRegistry;
import com.everhomes.rest.rpc.server.AclinkRemotePdu;
import com.everhomes.rest.rpc.server.ClientForwardPdu;
import com.everhomes.rest.rpc.server.DeviceRequestPdu;
import com.everhomes.rest.rpc.server.PingRequestPdu;
import com.everhomes.rest.rpc.server.PingResponsePdu;
import com.everhomes.rest.rpc.server.PusherNotifyPdu;

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
                
                messageClassRegistry.registerNameAnnotatedClass(PingRequestPdu.class);
                messageClassRegistry.registerNameAnnotatedClass(PingResponsePdu.class);
                messageClassRegistry.registerNameAnnotatedClass(DeviceRequestPdu.class);
                messageClassRegistry.registerNameAnnotatedClass(AclinkRemotePdu.class);
                messageClassRegistry.registerNameAnnotatedClass(ClientForwardPdu.class);
                messageClassRegistry.registerNameAnnotatedClass(PusherNotifyPdu.class);
            }
        }
    }
}
