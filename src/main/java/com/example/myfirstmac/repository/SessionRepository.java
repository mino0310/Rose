package com.example.myfirstmac.repository;

import com.example.myfirstmac.domain.session.Session;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, Long> {

    Optional<Session> findByAccessToken(String accessToken);

}
