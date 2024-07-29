package com.example.ewallet.transaction.service.Impl;

import com.example.ewallet.transaction.service.TransactionService;
import com.example.ewallet.transaction.service.resource.NotificationRequest;
import com.example.ewallet.transaction.service.resource.TransactionRequest;
import com.example.ewallet.transaction.service.resource.WalletTransactionRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    RestTemplate restTemplate;

    public ObjectMapper mapper = new ObjectMapper();

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public boolean performTransaction(Long senderId, TransactionRequest transactionRequest) throws JsonProcessingException {
        WalletTransactionRequest walletTransactionRequest = new WalletTransactionRequest();
        walletTransactionRequest.setSenderId(senderId);
        walletTransactionRequest.setReceiverId(transactionRequest.getReceiverId());
        walletTransactionRequest.setAmount(transactionRequest.getAmount());
        walletTransactionRequest.setTransactionType(transactionRequest.getTransactionType());
        String url = "http://localhost:8082/wallet/transaction";
        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, walletTransactionRequest, Boolean.class);
        String content= Strings.EMPTY;
        if(response.getStatusCode().is2xxSuccessful()){
            //send notification to both sender and receiver
            NotificationRequest senderNotificationRequest = new NotificationRequest();
            senderNotificationRequest.setUserId(senderId);
            senderNotificationRequest.setAmount(transactionRequest.getAmount());
            senderNotificationRequest.setUserType("sender");
            senderNotificationRequest.setTransactionStatus("success");
            content = mapper.writeValueAsString(senderNotificationRequest);
            kafkaTemplate.send("notification-topic", content);

            NotificationRequest receiverNotificationRequest = new NotificationRequest();
            receiverNotificationRequest.setUserId(transactionRequest.getReceiverId());
            receiverNotificationRequest.setAmount(transactionRequest.getAmount());
            receiverNotificationRequest.setUserType("receiver");
            receiverNotificationRequest.setTransactionStatus("success");
            content = mapper.writeValueAsString(receiverNotificationRequest);
            kafkaTemplate.send("notification-topic", content);
        }else{
            //send notification only for sender
            NotificationRequest senderNotificationRequest = new NotificationRequest();
            senderNotificationRequest.setUserId(senderId);
            senderNotificationRequest.setAmount(transactionRequest.getAmount());
            senderNotificationRequest.setUserType("sender");
            senderNotificationRequest.setTransactionStatus("failed");
            content = mapper.writeValueAsString(senderNotificationRequest);
            kafkaTemplate.send("notification-topic", content);
        }
        return response.getStatusCode().is2xxSuccessful();
    }
}
