package com.example.myfirstmac.controller;

import com.example.myfirstmac.config.data.UserSession;
import com.example.myfirstmac.domain.post.Post;
import com.example.myfirstmac.request.PostCreate;
import com.example.myfirstmac.request.PostEdit;
import com.example.myfirstmac.request.PostSearch;
import com.example.myfirstmac.response.PostResponse;
import com.example.myfirstmac.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @GetMapping("/foo")
    public Long foo(UserSession userSession, HttpServletRequest request) {

        // AuthResolver 에 의해서 유저가 보낸 JWT 안에서 userId를 가져와서 userSession안에 들어가게 된다.
        // authResolver 를 통해서 인증을 진행헀으므로 인증 역시 진행되었음을 의미한다.

        log.info(">>> userId = {} ", userSession.getId());

        HttpSession session = request.getSession();
        return userSession.getId();
    }

    @GetMapping("/bar")
    public String bar(UserSession userSession) {
        return "인증이 필요없는 페이지";
    }


    @PostMapping("/posts")
    public void createPost(@RequestBody @Valid PostCreate postCreate) throws Exception {

        // 인증 방법
        // 1. Get parameter -> 좋다고 할 순 없으나 실무에서 많이 쓰인다. 좋지 않은 이유는 url 에 노출되어서?
        // 2. Post body -> DTO 의 설계가 바뀌어야 함으로 쓰지 않음.
        // 3. Header -> @RequestHeader 를 사용

        postCreate.validate();
        postService.create(postCreate);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse getPost(@PathVariable(value = "postId") Long postId) {
        return postService.getPost(postId);

    }


    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public void editPost(@PathVariable(value = "postId") Long postId, @RequestBody PostEdit postEdit) {
        postService.edit(postId, postEdit);
    }


    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable("postId") Long postId) {
        postService.delete(postId);
    }
}
