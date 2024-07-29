package com.example.ewallet.notification.service.impl;

import com.example.ewallet.notification.exception.TransactionException;
import com.example.ewallet.notification.service.JavaMailUtils;
import com.example.ewallet.notification.service.NotificationService;
import com.example.ewallet.notification.service.response.NotificationRequest;
import com.example.ewallet.notification.service.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.logging.Logger;

@Service
public class NotificationServiceImpl implements NotificationService {

    Logger logger = Logger.getLogger(NotificationService.class.getName());
    String senderUserName = "";
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    JavaMailSender mailSender;


    @Override
    public void sendCommunication(NotificationRequest notificationRequest) {
        try {
            UserResponse userResponse = restTemplate.getForEntity("http://USER/api/users/" + notificationRequest.getUserId(), UserResponse.class).getBody();

            if (userResponse != null) {
                SimpleMailMessage message = new SimpleMailMessage();
                String subject;
                String content;
                if (notificationRequest.getUserType().equalsIgnoreCase("sender") && notificationRequest.getTransactionStatus().equalsIgnoreCase("success")) {
                    senderUserName = userResponse.getUsername();
                    subject = JavaMailUtils.getSubjectSenderSuccessfulTransaction(userResponse.getUsername(), notificationRequest.getAmount());
                    content = JavaMailUtils.getSuccessSenderEmailContent(userResponse.getUsername(), notificationRequest.getAmount());
                    logger.info("Sending mail: " + content);
                } else if (notificationRequest.getUserType().equalsIgnoreCase("receiver") && notificationRequest.getTransactionStatus().equalsIgnoreCase("success")) {
                    subject = JavaMailUtils.getSubjectReceiverSuccessfulTransaction(userResponse.getUsername(), notificationRequest.getAmount());
                    content = JavaMailUtils.getSuccessReceiverEmailContent(userResponse.getUsername(), senderUserName, notificationRequest.getAmount());
                    logger.info("Sending mail: " + content);
                } else if (notificationRequest.getUserType().equalsIgnoreCase("sender") && notificationRequest.getTransactionStatus().equalsIgnoreCase("failed")) {
                    subject = JavaMailUtils.getSubjectFailedTransaction(userResponse.getUsername(), notificationRequest.getAmount());
                    content = JavaMailUtils.getFailedSenderEmailContent(userResponse.getUsername(), notificationRequest.getAmount());
                    logger.info("Sending mail: " + content);
                } else {
                    logger.info("Unsupported user type or transaction status");
                    throw new TransactionException("INVALID_INPUT", "Unsupported user type or transaction status");
                }

                message.setSubject(subject);
                message.setTo(userResponse.getEmail());
                message.setText(content);
                mailSender.send(message);
            } else {
                logger.info("User response is null for userId: " + notificationRequest.getUserId());
                throw new TransactionException("USER_NOT_FOUND", "User response is null for userId: " + notificationRequest.getUserId());
            }
        } catch (HttpClientErrorException e) {
            logger.info("Error sending communication: " + e.getMessage());
            throw new TransactionException("HTTP_ERROR", "Error sending communication: " + e.getMessage());
        } catch (ResourceAccessException e) {
            logger.info("Error accessing resource: " + e.getMessage());
            throw new TransactionException("RESOURCE_ACCESS_ERROR", "Error accessing resource: " + e.getMessage());
        } catch (Exception e) {
            logger.info("An unexpected error occurred while sending communication: " + e.getMessage());
            throw new TransactionException("UNKNOWN_ERROR", "An unexpected error occurred while sending communication: " + e.getMessage());
        }
    }
}
