package com.example.myfirstmac.repository;

import com.example.myfirstmac.domain.post.Post;
import com.example.myfirstmac.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    public List<Post> getList(PostSearch postSearch);
}

