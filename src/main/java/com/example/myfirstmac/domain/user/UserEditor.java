package com.example.myfirstmac.domain.user;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserEditor {

    /** Editor 클래스는 수정을 위해서만 작성된 클래스이므로
     * 수정을 할 수 있는 필드만 정의한다.
     * 이를 쓰는 이유
     * 1. 수정 항목이 명확해진다.
     * 2.
     */

    private final String name;
    private final String address;


    // Builder Class 는 생성자 이전에 호출된다. 그리고 그렇게 걸러진 값이 생성자를 찾아서 매개변수로 들어가게 된다.
    @Builder
    public UserEditor(String name, String address) {
        this.name = name;
        this.address = address;
    }
}