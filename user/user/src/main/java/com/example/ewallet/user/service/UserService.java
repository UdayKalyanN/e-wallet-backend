package com.example.ewallet.user.service;

import com.example.ewallet.user.domain.User;
import com.example.ewallet.user.service.resource.TransactionRequest;
import com.example.ewallet.user.service.resource.UserRequest;
import com.example.ewallet.user.service.resource.UserResponse;

public interface UserService {

    public void createUser(User user);

    public UserResponse getUser(String id);

    public User login(String email, String username,String passWord);

    public User updateUser(UserRequest userRequest, String id);

    public User deleteUser(String id);

    public boolean transferFunds(Long senderId, TransactionRequest transactionRequest);
}
