package com.example.myfirstmac;

import com.example.myfirstmac.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

// 특정 클래스를 스프링 빈으로 등록시켜주는 어노테이션인 @ConfiguratinoPropreties 를 사용가능하게 해주는 것.
@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class MyFirstMacApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyFirstMacApplication.class, args);
    }

}
