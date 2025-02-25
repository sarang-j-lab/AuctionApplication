package com.AuctionApp.Auction.Config;

import com.AuctionApp.Auction.controllers.PanelController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry){
            registry.addEndpoint("/ws-auction") // WebSocket endpoint
                    .setAllowedOrigins("http://localhost:3000")
                    .setAllowedOriginPatterns("*") // Allow React frontend
                    .withSockJS(); // Enable SockJS for fallback
        }



    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        registry.enableSimpleBroker("/live"); // Message broker for clients
        registry.setApplicationDestinationPrefixes("/app"); // Prefix for client-to-server communication
    }


}
