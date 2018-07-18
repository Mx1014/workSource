// @formatter:off
package com.everhomes.border;

import com.everhomes.controller.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusMessageClassRegistry;
import com.everhomes.bus.LocalBusProvider;

@Configuration
public class WebSocketConfig implements WebSocketConfigurer {
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(65536);
        container.setMaxBinaryMessageBufferSize(65536);
        container.setAsyncSendTimeout(8000);
        container.setMaxSessionIdleTimeout(20000);//20s
        return container;
    }
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//    	registry.addHandler(clientHandler(), "/client");
//        registry.addHandler(pusherHandler(), "/pusher");
//        registry.addHandler(interServerHandler(), "/interserver");
//        registry.addHandler(aclinkHandler(), "/aclink/**").addInterceptors(aclinkInterceptors());
    	//增加.setAllowedOrigins("*"),解决wss连接时报文头origin的问题,待配置问题完全修复后改回来 by liuyilin 20180714
        registry.addHandler(clientHandler(), "/client").setAllowedOrigins("*");
        registry.addHandler(pusherHandler(), "/pusher").setAllowedOrigins("*");
        registry.addHandler(interServerHandler(), "/interserver");
        registry.addHandler(aclinkHandler(), "/aclink/**").setAllowedOrigins("*").addInterceptors(aclinkInterceptors());
    }

    @Bean
    public ClientWebSocketHandler clientHandler() {
        return new ClientWebSocketHandler();
    }
    
    @Bean
    public PusherWebSocketHandler pusherHandler() {
        return new PusherWebSocketHandler();
    }
    
    @Bean 
    public WebSocketHandler interServerHandler() {
        return new ServerWebSocketHandler();
    }
    
    @Bean
    public AclinkWebSocketHandler aclinkHandler() {
        return new AclinkWebSocketHandler();
    }
    
    @Bean  
    public HandshakeInterceptor aclinkInterceptors() {  
        return new AclinkHandshakeInterceptor();  
    }
    
    @Bean
    public LocalBus localBus() {
        return new LocalBusProvider();
    }
    
    @Bean
    public LocalBusMessageClassRegistry localBusMessageClassRegistry() {
        return new LocalBusMessageClassRegistry();
    }
}

