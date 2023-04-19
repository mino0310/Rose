package com.example.myfirstmac.controller;


import com.example.myfirstmac.config.AppConfig;
import com.example.myfirstmac.request.Login;
import com.example.myfirstmac.request.Signup;
import com.example.myfirstmac.response.SessionResponse;
import com.example.myfirstmac.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final AppConfig appConfig;

    // 암복호화에 사용하는 KEY

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {

        Long loggedUserId = authService.signin(login);
//        String accessToken = sessionResponse.getAccessToken();
//        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);


        // 암호화에 사용될 키를 바이트 배열로 디코드한 다음에 이로 암호화 키 객체를 생성한다.
        // 이 값이 시그니처를 만들어내는 암호화 키로 사용됨.
        // 유저 로그인 시마다 새로운 키를 생성해서 사용하지 않기 위해서 키를 정적으로 선언해서 사용한다.
        SecretKey secretKey = Keys.hmacShaKeyFor(appConfig.getJwtKey());


        String jws = Jwts.builder()
                .setSubject(String.valueOf(loggedUserId))
                .signWith(secretKey)
                .setIssuedAt(new Date())
                .compact();

        return SessionResponse.builder().accessToken(jws).build();
    }

    @PostMapping("/auth/signup")
    public void signup(@RequestBody Signup signup) {

        // 나중에는 컨트롤러 단에서 변환해서 서비스 단으로 엔티티를 넘겨주는 방식으로 처리하도록 수정할 것
        authService.signup(signup);
    }
}
