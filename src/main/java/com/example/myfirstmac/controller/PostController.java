package com.example.myfirstmac.controller;

import com.example.myfirstmac.domain.post.Post;
import com.example.myfirstmac.request.PostCreate;
import com.example.myfirstmac.request.PostSearch;
import com.example.myfirstmac.response.PostResponse;
import com.example.myfirstmac.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void createPost(@RequestBody @Valid PostCreate postCreate) throws Exception {

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
}
