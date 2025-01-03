package com.AuctionApp.Auction.ExceptionHandling;


import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class CustomException extends RuntimeException{
    private String message;
    private HttpStatus errorCode;
    private LocalDateTime timestamp = LocalDateTime.now();
    private String details;

    public CustomException(String message, HttpStatus errorCode, String details) {
        this.message = message;
        this.errorCode = errorCode;
        this.details = details;
    }
}
