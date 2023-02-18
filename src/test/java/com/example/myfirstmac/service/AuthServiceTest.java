package com.example.myfirstmac.service;

import com.example.myfirstmac.exception.UserNotFound;
import com.example.myfirstmac.repository.UserRepository;
import com.example.myfirstmac.request.Login;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthService authService;

    @BeforeEach
    void clean() {
    }

    @Test
    @DisplayName("존재하지 않는 아이디를 입력할 경우 UserNotFound 예외를 반환한다.")
    void idNotExist() {
        // given
        // 로그인 객체 직접 생성

        Login login = Login.builder().email("bewriter310@naver.com").password("mino93").name("김민호").build();

        // when
        // signin call
        Assertions.assertThrows(UserNotFound.class, () -> authService.signin(login));

        // then
        // Exception call
    }


    @Test
    @DisplayName("존재하지 않는 비밀번호를 입력할 경우 UserNotFound 예외를 반환한다.")
    void passwordNotExist(){
        //given
        Login login = Login.builder().email("mino_0310@naver.com").password("mino93333").name("김민호").build();

        // when
        // signin call
        Assertions.assertThrows(UserNotFound.class, () -> authService.signin(login));

        // then
    }

    @Test
    @DisplayName("존재하는 유저 정보를 입력할 경우 유저 로그인을 통과한다.")
    void login() {
        //given
        Login login = Login.builder().email("mino_0310@naver.com").password("mino93").name("김민호").build();


        // when
        authService.signin(login);

        // then
    }

}