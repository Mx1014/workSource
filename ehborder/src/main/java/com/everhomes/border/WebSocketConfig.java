// @formatter:off
package com.everhomes.border;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusMessageClassRegistry;
import com.everhomes.bus.LocalBusProvider;
import com.everhomes.controller.ClientWebSocketHandler;
import com.everhomes.controller.ServerWebSocketHandler;

@Configuration
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(clientHandler(), "/client");
        registry.addHandler(interServerHandler(), "/interserver");
    }

    @Bean
    public WebSocketHandler clientHandler() {
        return new ClientWebSocketHandler();
    }
    
    @Bean 
    public WebSocketHandler interServerHandler() {
        return new ServerWebSocketHandler();
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

