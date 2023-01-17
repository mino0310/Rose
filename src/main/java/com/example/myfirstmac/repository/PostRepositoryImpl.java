package com.example.myfirstmac.repository;

import com.example.myfirstmac.domain.post.Post;
import com.example.myfirstmac.domain.post.QPost;
import com.example.myfirstmac.request.PostSearch;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{


    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
        List<Post> userList = jpaQueryFactory.selectFrom(QPost.post)
                .limit(postSearch.getSize() == null ? 1 : postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(QPost.post.id.desc())
                .fetch();

        return userList;
    }
}
