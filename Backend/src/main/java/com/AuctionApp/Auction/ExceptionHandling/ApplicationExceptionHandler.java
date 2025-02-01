package com.AuctionApp.Auction.ExceptionHandling;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.validation.UnexpectedTypeException;
import jakarta.validation.ValidationException;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.security.SignatureException;
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


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnexpectedTypeException.class)
    public RuntimeException handleInvalidConstrains(UnexpectedTypeException exception){
            return new RuntimeException(exception.getMessage());
    }


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomException> handleCustomException(CustomException exception){
        return new ResponseEntity<>(new CustomException(exception.getMessage(),exception.getErrorCode(),exception.getDetails()),HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RuntimeException> handleTypeException(HttpMessageNotReadableException exception){
        if(exception.getMessage().equals("JSON parse error: Unexpected character ('}' (code 125)): was expecting double-quote to start field name")){
            return new ResponseEntity(new RuntimeException("provide valid JSON"),HttpStatus.BAD_REQUEST);
        }
        if(exception.getMessage().equals("JSON parse error: Cannot deserialize value of type `java.util.Date` from String \"22-01-2025\": expected format \"MM:dd:yyyy\"")){
            return new ResponseEntity(new RuntimeException("Date should in pattern of MM:dd:yyyy"),HttpStatus.BAD_REQUEST);
        }
        System.out.println(exception);
        return new ResponseEntity(new RuntimeException(exception.getMessage()),HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<RuntimeException> handleUniqueConstrainsException(SQLIntegrityConstraintViolationException exception){
        return new ResponseEntity<>(new RuntimeException(exception.getMessage()),HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<RuntimeException> handleValidationError(ValidationException exception){
        return new ResponseEntity<>(new RuntimeException(exception.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(io.jsonwebtoken.ExpiredJwtException.class)
    public ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired.");
    }

    @ExceptionHandler(io.jsonwebtoken.MalformedJwtException.class)
    public ResponseEntity<String> handleMalformedJwtException(MalformedJwtException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JWT token.");
    }

    @ExceptionHandler(io.jsonwebtoken.SignatureException.class)
    public ResponseEntity<String> handleSignatureException(SignatureException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token signature.");
    }

    @ExceptionHandler(org.springframework.security.core.userdetails.UsernameNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found! please try again!.");
    }

}


