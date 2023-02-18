package com.example.myfirstmac.service;


import com.example.myfirstmac.domain.session.Session;
import com.example.myfirstmac.domain.user.User;
import com.example.myfirstmac.exception.UserNotFound;
import com.example.myfirstmac.repository.UserRepository;
import com.example.myfirstmac.request.Login;
import com.example.myfirstmac.response.SessionResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;


    @Transactional
    public SessionResponse signin(Login login) {
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow( UserNotFound::new);

        Session session = user.addSession();

        SessionResponse sessionResponse = SessionResponse.builder().accessToken(session.getAccessToken()).build();


        return sessionResponse;


    }



}
