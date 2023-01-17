package com.example.myfirstmac.response;

import com.example.myfirstmac.domain.post.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponse {

    private String title;
    private String content;

    @Builder
    public PostResponse(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostResponse(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
