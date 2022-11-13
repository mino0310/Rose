package com.example.myfirstmac.response;


import com.example.myfirstmac.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * 서비스 정책에 맞는 클래스
 * 엔티티에 서비스 관련 정책을 넣어선 절대 안 된다. 엔티티는 순수하게 유지해야 한다.
 */
@Getter
public class UserResponse {

    private Long id;
    private String userId;
    private String name;
    private String address;

    // 생성자 오버로딩
    public UserResponse(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.name = user.getName();
        this.address = user.getAddress();
    }

    @Builder
    public UserResponse(Long id, String userId, String name, String address) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.address = address;
    }
}
