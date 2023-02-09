package com.example.myfirstmac.service;

import com.example.myfirstmac.domain.post.Post;
import com.example.myfirstmac.domain.post.PostEditor;
import com.example.myfirstmac.exception.PostNotFound;
import com.example.myfirstmac.repository.PostRepository;
import com.example.myfirstmac.request.PostCreate;
import com.example.myfirstmac.request.PostEdit;
import com.example.myfirstmac.request.PostSearch;
import com.example.myfirstmac.response.PostResponse;
import com.example.myfirstmac.response.UserResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Long create(PostCreate postCreate) {

        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        Long id = postRepository.save(post).getId();

        return id;
    }

    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFound::new);

        PostResponse postResponse = PostResponse.builder()
                .id(post.getId() )
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        return postResponse;
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFound::new);
        postRepository.delete(post);
    }


    public List<PostResponse> getList(PostSearch postSearch) {
        List<PostResponse> postList = postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());

        return postList;
    }

    @Transactional
    public void edit(Long id,PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();

        PostEditor postEditor = postEditorBuilder
                .title(postEdit.getTitle() != null ? postEdit.getTitle() : post.getTitle())
                .content(postEdit.getContent() != null ? postEdit.getContent() : post.getContent())
                .build();
        post.edit(postEditor);
    }
}
