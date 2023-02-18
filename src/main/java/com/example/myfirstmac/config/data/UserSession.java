package com.example.myfirstmac.config.data;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSession {

    private Long id;

    @Builder
    public UserSession(Long id) {
        this.id = id;
    }
}

