package com.example.myfirstmac.repository;

import com.example.myfirstmac.domain.user.QUser;
import com.example.myfirstmac.domain.user.User;
import com.example.myfirstmac.request.UserSearch;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.myfirstmac.domain.user.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{

    // QueryDsl 을 사용하려면 JPA 쿼리 팩토리를 만들어줘야하는데, 직접 만들어서 주입해줘야 한다.

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<User> getList(UserSearch userSearch) {


        return jpaQueryFactory.selectFrom(user)
                .limit(userSearch.getSize() == null ? 1 : userSearch.getSize())
                .offset(userSearch.getOffset())
                .orderBy(user.id.desc()) // pk 값으로 Order by
                .fetch();
    }
}
