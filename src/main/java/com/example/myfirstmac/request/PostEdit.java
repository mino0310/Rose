package com.example.myfirstmac.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PostEdit {

    //필드
    private String title;
    private String content;

    // 생성자
    @Builder
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
