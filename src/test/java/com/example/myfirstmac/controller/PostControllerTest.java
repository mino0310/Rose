package com.example.myfirstmac.controller;

import com.example.myfirstmac.domain.post.Post;
import com.example.myfirstmac.exception.PostNotFound;
import com.example.myfirstmac.repository.PostRepository;
import com.example.myfirstmac.request.PostCreate;
import com.example.myfirstmac.request.PostEdit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
// 서비스도 테스트하고 레파지토리도 테스트하는 전반적인 스프링을 이용해야 한다면 스프링을 띄워야 하므로 다음 어노테이션을 사용한다.
@SpringBootTest
public class PostControllerTest {


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void cleanData(){
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 작성 요청 시 DB에 값이 입력된다. ")
    void test1() throws Exception {

        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목")
                .content("내용")
                .build();

        String json = objectMapper.writeValueAsString(postCreate);

        // when
        // 요청 보내기
        this.mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // then
        long count = postRepository.findAll().stream().count();
        Assertions.assertEquals(1L, count);
    }

    @Test
    @DisplayName("글 수정 요청 시 글을 수정한다.")
    void test2() throws Exception {

        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        Post savedPost = this.postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("변경제목")
                .content("변경내용")
                .build();

        String json = objectMapper.writeValueAsString(postEdit);

        this.mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", savedPost.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print());

        // Expected
        Post foundPost = postRepository.findById(savedPost.getId())
                .orElseThrow(PostNotFound::new);
        Assertions.assertEquals("변경제목", foundPost.getTitle());
        Assertions.assertEquals("변경내용", foundPost.getContent());

    }

    @Test
    @DisplayName("삭제 요청 시 게시물을 삭제한다")
    void test3() throws Exception{
        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        Post savedPost = this.postRepository.save(post);

        // Expected
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}", savedPost.getId()))
                .andDo(MockMvcResultHandlers.print());
        long count = postRepository.findAll().stream().count();
        Assertions.assertEquals(0L, count);

    }

/*    @Test
    @DisplayName("테스트 요청 시 accessToken값이 정확할 때만 때만 hello를 반환한다.")
    void test4() throws Exception{
        // given
        // when
        this.mockMvc.perform(MockMvcRequestBuilders.get("/test")
                        .header("accessToken", "minomi"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("hello"))
                .andDo(MockMvcResultHandlers.print());

        // then
    }*/

}
