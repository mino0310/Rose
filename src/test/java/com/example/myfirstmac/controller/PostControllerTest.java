package com.example.myfirstmac.controller;

import com.example.myfirstmac.domain.post.Post;
import com.example.myfirstmac.domain.user.User;
import com.example.myfirstmac.repository.PostRepository;
import com.example.myfirstmac.request.PostCreate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.mino.com", uriPort = 443)
@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void cleanData(){
    }

    @Test
    @DisplayName("게시글 생성 여부 확인")
    void test1() throws Exception {
        PostCreate postCreate = PostCreate.builder()
                .title("제목")
                .content("내용")
                .build();

        String json = objectMapper.writeValueAsString(postCreate);

        this.mockMvc.perform(post("/posts", json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentation.document("post-create",
                    PayloadDocumentation.requestFields(
                            PayloadDocumentation.fieldWithPath("title").description("제목"),
                            PayloadDocumentation.fieldWithPath("content").description("내용")
                    )
                ));
    }

    @Test
    @DisplayName("게시글 생성시 제목은 필수다")
    void test2() throws Exception{
        PostCreate postCreate = PostCreate.builder()
                .content("본문입니다.")
                .build();

        String json = objectMapper.writeValueAsString(postCreate);

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    @DisplayName("게시글 단건 조회")
    void test3() throws Exception {
        // given
        Post post = Post.builder().title("제목입니다.")
                .content("본문입니다.")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", post.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("제목입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("본문입니다."));
    }

    @Test
    @DisplayName("게시글 여러 건 조회")
    void test4() throws Exception {

        // when
        List<Post> list = IntStream.range(0, 30)
                .mapToObj(i -> Post.builder().title("title" + i).content("content" + i).build()).collect(Collectors.toList());

        postRepository.saveAll(list);

        Assertions.assertEquals(30L, postRepository.count());

        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=1&size=10"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("title29"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("content29"));

    }
}