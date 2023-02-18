package com.example.myfirstmac.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SessionResponse {

    private String accessToken;

    @Builder
    public SessionResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
