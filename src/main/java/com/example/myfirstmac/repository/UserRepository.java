package com.example.myfirstmac.repository;

import com.example.myfirstmac.domain.user.User;
import com.example.myfirstmac.service.AuthService;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
}
