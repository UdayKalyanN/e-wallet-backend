package com.example.ewallet.user.domain;

import com.example.ewallet.user.service.resource.UserResponse;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@Table(name = "users")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long id;
    private String username;
    private String email;
    private String password;

    public UserResponse toUserResponse() {
        return UserResponse.builder().
                id(String.valueOf(id)).
                username(username).
                email(email).
                build();
    }
}
