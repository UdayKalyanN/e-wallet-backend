package com.example.ewallet.notification.consumer;

import com.example.ewallet.notification.service.NotificationService;
import com.example.ewallet.notification.service.response.NotificationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class NotificationConsumer {

    @Autowired
    NotificationService notificationService;

    Logger logger = Logger.getLogger(NotificationConsumer.class.getName());
    ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "notification-topic", groupId = "notificationGrp")
    public void consumer(String message){
        logger.info("Received message: " + message);
        try{
            NotificationRequest notificationRequest = objectMapper.readValue(message, NotificationRequest.class);
            notificationService.sendCommunication(notificationRequest);
            logger.info("Received notification request: " + notificationRequest.toString());
        }catch (Exception e){
            logger.info("Exception: " + e.getMessage());
        }
    }
}
