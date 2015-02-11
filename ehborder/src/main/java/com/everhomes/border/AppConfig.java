// @formatter:off
package com.everhomes.border;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@Configuration
@ImportResource(value="classpath*:**/applicationContext.xml")
@EnableWebSocket
@EnableWebSocketMessageBroker
@EnableScheduling
public class AppConfig {
}
