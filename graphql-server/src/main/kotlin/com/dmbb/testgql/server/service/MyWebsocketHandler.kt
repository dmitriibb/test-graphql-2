package com.dmbb.testgql.server.service

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
//@Component
@EnableWebSocket
class MyWebsocketHandler: WebSocketMessageBrokerConfigurer {
//    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
//        registry.addEndpoint("/ws")
//            .setAllowedOriginPatterns("*")
//    }
}