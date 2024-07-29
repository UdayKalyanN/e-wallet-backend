package com.example.ewallet.notification.service;

import com.example.ewallet.notification.service.response.NotificationRequest;
import com.example.ewallet.notification.service.response.UserResponse;

public interface NotificationService {

    public void sendCommunication(NotificationRequest notificationRequest);

}
