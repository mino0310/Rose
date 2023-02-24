package com.example.myfirstmac.service;

import com.example.myfirstmac.domain.user.User;
import com.example.myfirstmac.exception.AlreadyEmailExist;
import com.example.myfirstmac.exception.AuthenticateNotValid;
import com.example.myfirstmac.exception.UserNotFound;
import com.example.myfirstmac.repository.UserRepository;
import com.example.myfirstmac.request.Login;
import com.example.myfirstmac.request.Signup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
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
    @DisplayName("존재하지 않는 아이디를 입력할 경우 AuthenticateNotValid 예외를 반환한다.")
    void idNotExist() {
        // given
        // 로그인 객체 직접 생성

        Login login = Login.builder().email("bewriter310@naver.com").password("mino93").name("김민호").build();

        // when
        // signin call
        Assertions.assertThrows(AuthenticateNotValid.class, () -> authService.signin(login));

        // then
        // Exception call
    }


    @Test
    @DisplayName("존재하지 않는 비밀번호를 입력할 경우 AuthenticateNotValid 예외를 반환한다.")
    void passwordNotExist(){
        //given
        Login login = Login.builder().email("mino_0310@naver.com").password("mino93333").name("김민호").build();

        // when
        // signin call
        Assertions.assertThrows(AuthenticateNotValid.class, () -> authService.signin(login));

        // then
    }

    @Test
    @DisplayName("존재하는 유저 정보를 입력할 경우 유저 로그인을 통과한다.")
    void login() {
        //given
        // 회원가입
        Signup signup = Signup.builder()
                .email("mino_0310@naver.com").password("mino93").name("김민호").build();
        authService.signup(signup);

        Login login = Login.builder().email("mino_0310@naver.com").password("mino93").name("김민호").build();

        // when
        authService.signin(login);

        // then
    }

    @Test
    @DisplayName("유저가 이름, 비밀번호, 이메일을 입력해 회원가입 요청 시 회원가입 성공")
    void singup() {
        // given
        // Signup 객체 정보 입력 (컨트롤러에서 넘어오는 거)
        Signup signup = Signup.builder()
                .name("mino")
                .password("1234")
                .email("mino0310@naver.com")
                .build();

        // when
        // 회원가입
        authService.signup(signup);

        // then
        // 정보 맞는지 조회
        Assertions.assertEquals(1L, userRepository.count());

        User savedUser = userRepository.findAll().iterator().next();

        Assertions.assertEquals("mino", savedUser.getName());
        Assertions.assertNotNull(savedUser.getPassword());
        Assertions.assertEquals("mino0310@naver.com", savedUser.getEmail());


    }

    @Test
    @DisplayName("중복된 이메일로 가입 요청 시 실패")
    void existEmailFail() {

        // given
        User user = userRepository.save(User.builder().email("mino0310@naver.com")
                .name("aaaa")
                .password("123333")
                .build());

        // 회원 가입 객체
        Signup signup = Signup.builder()
                .email("mino0310@naver.com")
                .name("mino")
                .password("1234")
                .build();

        // expected

        Assertions.assertThrows(AlreadyEmailExist.class, () -> {
            // 회원 가입 요청
            authService.signup(signup);
        });

    }



}