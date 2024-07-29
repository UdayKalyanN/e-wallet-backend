package com.example.ewallet.transaction.service;


import com.example.ewallet.transaction.service.resource.TransactionRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface TransactionService {


    public boolean performTransaction(Long senderId, TransactionRequest transactionRequest) throws JsonProcessingException;
}
