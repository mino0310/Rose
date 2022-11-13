package com.example.myfirstmac.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class UserEdit {

//    UserEdit 과 UserCreate 가 모양이 비슷하다고 하나의 클래스에서 처리하면 안 된다.
    // 기능이 다르다면 명확하게 분리하는 것이 옳다.

    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @NotBlank
    private String address;

    @Builder
    public UserEdit(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
