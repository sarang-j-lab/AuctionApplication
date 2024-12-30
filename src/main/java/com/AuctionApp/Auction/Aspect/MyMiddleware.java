package com.AuctionApp.Auction.Aspect;

import com.AuctionApp.Auction.DTO.AuctionDTO;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;


@Component
public class MyMiddleware extends OncePerRequestFilter {

        private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(request.getRequestURI().equals("/auction/new-auction")){
            CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);

            AuctionDTO bodyObject = objectMapper.readValue(cachedRequest.getInputStream(), AuctionDTO.class);

//            System.out.println(bodyObject);
            filterChain.doFilter(cachedRequest,response);
        }else{
        filterChain.doFilter(request, response);
        }
    }

}

