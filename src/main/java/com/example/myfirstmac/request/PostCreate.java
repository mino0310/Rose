package com.example.myfirstmac.request;

import com.example.myfirstmac.exception.InvalidPostRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class PostCreate {

    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate() {
        if (title.contains("욕설")) {
            throw new InvalidPostRequest("title", "제목에 욕설을 포함할 수 없습니다.");

        }
    }
}
