package com.example.myfirstmac.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Base64;

@ConfigurationProperties(prefix = "mino")
@Setter
@Getter
public class AppConfig {

//    private String test;
    // 변수 명이 application.properties 에 적힌 것과 같아야지 매칭이 됨. 이름으로 찾네
    private String jwtKey;

    // 사용성을 위해서 반환 과정에서 디코딩해서 바이트 배열로 내려준다.
    // 바이트 배열로 내려줘야 시크릿키로 변환하는 곳에 매개변수로 던질 수 있다.
    public byte[] getJwtKey() {
        return Base64.getDecoder().decode(jwtKey);
    }
}
