package com.example.ewallet.user.repository;

import com.example.ewallet.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    User findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmailAndUsername(String email, String username);

}
