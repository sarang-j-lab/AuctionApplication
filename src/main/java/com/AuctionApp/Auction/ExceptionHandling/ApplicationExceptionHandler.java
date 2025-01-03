package com.AuctionApp.Auction.ExceptionHandling;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import jakarta.validation.UnexpectedTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleInvalidArguments(MethodArgumentNotValidException exception){
        Map<String,String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error->{
            errorMap.put(error.getField(),error.getDefaultMessage());
        });
        return errorMap;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public RuntimeException handleInternalServerError(HttpServerErrorException.InternalServerError exception){
        return new RuntimeException("Server is not responding due to internal server error");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsernameNotFoundException.class)
    public RuntimeException handleUserNotFoundException(UsernameNotFoundException exception){
        return new RuntimeException("User not found");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnexpectedTypeException.class)
    public RuntimeException handleInvalidConstrains(UnexpectedTypeException exception){
            return new RuntimeException(exception.getMessage());
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomException> handleCustomException(CustomException exception){
        return new ResponseEntity<>(new CustomException(exception.getMessage(),exception.getErrorCode(),exception.getDetails()),HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RuntimeException> handleTypeException(HttpMessageNotReadableException exception){
        if(exception.getMessage().equals("JSON parse error: Unexpected character ('}' (code 125)): was expecting double-quote to start field name")){
            return new ResponseEntity(new RuntimeException("provide valid JSON"),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(new RuntimeException("Player style should be one of: BATSMAN, BOWLER, BATSMAN_WICKET_KEEPER, ALL_ROUNDER"),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<RuntimeException> handleUniqueConstrainsException(SQLIntegrityConstraintViolationException exception){
        return new ResponseEntity<>(new RuntimeException(exception.getMessage()),HttpStatus.BAD_REQUEST);
    }

}
