package com.example.hakatonweatherapi;

import com.example.hakatonweatherapi.web.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientRequestException;

@RestControllerAdvice
public class ExceptionTranslator {

    @ExceptionHandler({WebClientRequestException.class})
    public ResponseEntity<ExceptionResponse> handleWebClientError(RuntimeException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse("Server error"));
    }
}
