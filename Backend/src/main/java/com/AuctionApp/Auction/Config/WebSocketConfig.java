package com.AuctionApp.Auction.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws-auction") // WebSocket endpoint
                .setAllowedOrigins("http://localhost:3000") // Allow React frontend
                .withSockJS() // Enable SockJS for fallback
                .setSessionCookieNeeded(false)  // Optional: Disable cookies
                .setWebSocketEnabled(true);
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        registry.enableSimpleBroker("/live"); // Message broker for clients
        registry.setApplicationDestinationPrefixes("/app"); // Prefix for client-to-server communication
    }

}
