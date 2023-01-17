package com.example.myfirstmac.domain.post;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditor {


    private String title;

    private String content;

    @Builder
    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
