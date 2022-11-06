package com.example.myfirstmac.request;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class UserCreate {

    @NotBlank(message = "아이디를 입력해주세요")
    private String userId;

    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @NotBlank
    private String address;

    // builder 는 생성자에 직접 달아주는 것이 좋다.
    @Builder
    public UserCreate(String userId, String name, String address) {
        this.userId = userId;
        this.name = name;
        this.address = address;
    }

    // Builder 의 장점
    // 1. 가독성
    // 2. 값 생성에 대한 유연함
    // 3. 필요한 값만 받을 수 있다. 생성자를 굳이 오버로딩할 필요가 없다.
    // 4. 객체의 불변성 (*핵심*)

}
