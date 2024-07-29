package com.example.ewallet.wallet.configuration;

import com.example.ewallet.wallet.exception.WalletException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class WalletControllerAdvice {
    @ExceptionHandler(WalletException.class)
    public ResponseEntity<?> handleUserException(WalletException exception){
        Map<String,String> errorMap = new HashMap<>();
        errorMap.put("type",exception.getType());
        errorMap.put("message",exception.getMessage());
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){

        Map<String,String> errorsMap=new HashMap<>();
        for(FieldError error:e.getBindingResult().getFieldErrors()){
            errorsMap.put(error.getField(),error.getDefaultMessage());

        }
        return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
    }
}
