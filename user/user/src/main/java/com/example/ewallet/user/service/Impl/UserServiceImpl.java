package com.example.ewallet.user.service.Impl;

import com.example.ewallet.user.domain.User;
import com.example.ewallet.user.exception.UserException;
import com.example.ewallet.user.feignClient.TransactionClient;
import com.example.ewallet.user.repository.UserRepository;
import com.example.ewallet.user.service.UserService;
import com.example.ewallet.user.service.resource.TransactionRequest;
import com.example.ewallet.user.service.resource.UserRequest;
import com.example.ewallet.user.service.resource.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Value("${kafka.topic.user-created}")
    private String USER_CREATED_TOPIC;

    @Value("${kafka.topic.user-deleted}")
    private String UserDeletedTopic;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    TransactionClient transactionClient;

    @Override
    public void createUser(User user) {
        //check if the user is valid
        //check if username exists
        Optional<User> userOptional=userRepository.findByUsername(user.getUsername());
        if(userOptional.isPresent()){
            throw new UserException("EWALLET_USER_EXISTS_EXCEPTION","User already exists");
        }
        //encode the password before storing
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //save the user to DB.
        userRepository.save(user);

        //create a user created event
        kafkaTemplate.send(USER_CREATED_TOPIC,String.valueOf(user.getId()));
    }

    @Override
    public UserResponse getUser(String id) {
        Optional<User> userOptional = userRepository.findById(Long.valueOf(id));
        User user= userOptional.orElseThrow(() -> new UserException("EWALLET_USER_NOT_FOUND_EXCEPTION","User not found"));
        return new UserResponse(user);
    }

    @Override
    public User login(String email, String username,String passWord) {

        Optional<User> userOptional= Optional.ofNullable(userRepository.findByEmail(email));
        if(userOptional.isEmpty()){
            throw new UserException("EWALLET_USER_NOT_FOUND_EXCEPTION","User not found");
        }
        if(!passwordEncoder.matches(passWord,userOptional.get().getPassword())){
            throw new UserException("EWAALLET_USER_PASSWORD_NOT_MATCH_EXCEPTION","Password not matched");
        }
        return userOptional.get();
    }

    @Override
    public User updateUser(UserRequest userRequest, String id) {
        Optional<User> userOptional = userRepository.findById(Long.valueOf(id));
        if(userOptional.isEmpty()){
            throw new UserException("EWALLET_USER_NOT_FOUND_EXCEPTION","User not found");
        }
        validateUser(userOptional.get(),userRequest);
        User user = userOptional.get();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userRepository.save(user);
        return user;
    }

    private void validateUser(User user, UserRequest userRequest) {
        if(user.getUsername().equals(userRequest.getUsername()) && user.getEmail().equals(userRequest.getEmail()) && user.getPassword().equals(userRequest.getPassword()) ){
            throw new UserException("EWALLET_USER_NOT_CHANGED_EXCEPTION","User details same as before");
        }

    }

    @Override
    public User deleteUser(String id) {
        Optional<User> userOptional = userRepository.findById(Long.valueOf(id));
        if(userOptional.isEmpty()){
            throw new UserException("EWALLET_USER_NOT_FOUND_EXCEPTION","User not found");
        }
        userRepository.deleteById(Long.valueOf(id));
        kafkaTemplate.send(UserDeletedTopic, String.valueOf(id));
        return userOptional.get();
    }

    @Override
    public boolean transferFunds(Long senderId, TransactionRequest transactionRequest) {
        Optional<User> senderOptional = userRepository.findById(senderId);
        if(senderOptional.isEmpty()){
            throw new UserException("EWALLET_USER_NOT_FOUND_EXCEPTION","User not found");
        }
        Optional<User> receiverOptional = userRepository.findById(transactionRequest.getReceiverId());
        if(receiverOptional.isEmpty()){
            throw new UserException("EWALLET_RECEIVER_NOT_FOUND_EXCEPTION","Receiver not found");
        }
        //call transaction microservice
        //ResponseEntity<Boolean> response = restTemplate.postForEntity("http://TRANSACTION/transaction/"+senderId+"/transfer", transactionRequest, Boolean.class);
        //using feign
        ResponseEntity<Boolean> response = transactionClient.transfer(String.valueOf(senderId),transactionRequest);
        return response.getBody();
    }
}
