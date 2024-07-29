package com.example.ewallet.transaction.controller;

import com.example.ewallet.transaction.service.TransactionService;
import com.example.ewallet.transaction.service.resource.TransactionRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/transaction/{senderId}/transfer")
    public ResponseEntity<Boolean> transfer(@PathVariable("senderId") String senderId, @RequestBody TransactionRequest transactionRequest) throws JsonProcessingException {
        boolean result = transactionService.performTransaction(Long.parseLong(senderId), transactionRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
