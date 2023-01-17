package com.example.myfirstmac.service;

import com.example.myfirstmac.domain.post.Post;
import com.example.myfirstmac.domain.user.User;
import com.example.myfirstmac.exception.PostNotFound;
import com.example.myfirstmac.repository.PostRepository;
import com.example.myfirstmac.request.PostCreate;
import com.example.myfirstmac.request.PostEdit;
import com.example.myfirstmac.request.PostSearch;
import com.example.myfirstmac.response.PostResponse;
import com.mysema.commons.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    PostService postService;

    @BeforeEach
    void cleanData() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 생성")
    void test1() {

        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("본문입니다.")
                .build();

        //when
        postService.create(postCreate);

        //then
        Assertions.assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        Assertions.assertEquals("제목입니다.", post.getTitle());
        Assertions.assertEquals("본문입니다.", post.getContent());
    }

    @Test
    @DisplayName("게시글 1개 조회")
    void test2() {
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("본문입니다.")
                .build();

        Long id = postService.create(postCreate);

        PostResponse post = postService.getPost(id);

        Assertions.assertNotNull(post);
        Assertions.assertEquals("제목입니다.", post.getTitle());
        Assertions.assertEquals("본문입니다.", post.getContent());
    }


    @Test
    @DisplayName("게시글 다수 조회")
    void test3() {

        // given
        List<Post> list = IntStream.range(0, 30)
                .mapToObj(i -> Post.builder().title("title" + i).content("content" + i).build()).collect(Collectors.toList());
        postRepository.saveAll(list);

        PostSearch postSearch = PostSearch.builder().size(10).page(1).build();

        // when
        List<PostResponse> postList = postService.getList(postSearch);

        // then
        Assertions.assertEquals("title29", postList.get(0).getTitle());
        Assertions.assertEquals("content29", postList.get(0).getContent());
        Assertions.assertEquals(10L, postList.size());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 삭제")
    void test4() {

        Assertions.assertThrows(PostNotFound.class, () -> {
            postService.delete(2L);
        });
    }

    @Test
    @DisplayName("게시글 제목 수정")
    void test5() {

        // given
        Post post = Post.builder().title("제목")
                .content("본문")
                .build();

        Post savedPost = postRepository.save(post);

        PostEdit postEdit = PostEdit.builder().title("변경된 제목")
                .content("변경된 본문")
                .build();

        // when
        postService.edit(savedPost.getId(), postEdit);
        Post findPost = postRepository.findById(savedPost.getId()).get();

        // then
        Assertions.assertEquals("변경된 제목", findPost.getTitle());
        Assertions.assertEquals("변경된 본문", findPost.getContent());
    }
}