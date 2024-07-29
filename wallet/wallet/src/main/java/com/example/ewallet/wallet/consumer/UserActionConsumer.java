package com.example.ewallet.wallet.consumer;

import com.example.ewallet.wallet.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserActionConsumer {

//    @Value("${kafka.topic.user-created}")
//    private String USER_CREATED_TOPIC;
//
//    @Value("${kafka.topic.user-deleted}")
//    private String UserDeletedTopic;

    Logger logger = LoggerFactory.getLogger(UserActionConsumer.class);

    @Autowired
    WalletService walletService;

    @KafkaListener(topics = "${kafka.topic.user-created}", groupId = "walletGrp")
    public void consume(String message) {
        logger.info(String.format("#### -> Consumed message -> %s", message));
        walletService.createWallet(Long.valueOf(message));
    }

    @KafkaListener(topics = "${kafka.topic.user-deleted}", groupId = "walletGrp")
    public void consumeDelete(String message) {
        logger.info(String.format("#### -> Consumed message -> %s", message));
        walletService.deleteWallet(Long.valueOf(message));
    }

}
