package com.example.ewallet.user.service.resource;


import com.example.ewallet.user.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {


    @NotBlank(message = "username cannot be blank")
    private String username;
    @Email(message = "email is not valid")
    private String email;
    @NotBlank(message = "password cannot be blank")
    private String password;


    public User toUser() {
       return User.builder().
               username(username).
               email(email).
               password(password).
               build();
    }
}
