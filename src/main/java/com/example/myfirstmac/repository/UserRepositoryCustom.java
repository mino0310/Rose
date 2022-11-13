package com.example.myfirstmac.repository;

import com.example.myfirstmac.domain.user.User;
import com.example.myfirstmac.request.UserSearch;

import java.util.List;

public interface UserRepositoryCustom {

    List<User> getList(UserSearch userSearch);
}
