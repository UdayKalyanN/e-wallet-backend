package com.example.ewallet.user.feignClient;


import com.example.ewallet.user.service.resource.TransactionRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "transaction")
public interface TransactionClient {

    @PostMapping("/transaction/{senderId}/transfer")
    public ResponseEntity<Boolean> transfer(@PathVariable("senderId") String senderId, @RequestBody TransactionRequest transactionRequest);

}

