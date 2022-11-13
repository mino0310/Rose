package com.example.myfirstmac.service;


import com.example.myfirstmac.domain.user.User;
import com.example.myfirstmac.repository.UserRepository;
import com.example.myfirstmac.request.UserCreate;
import com.example.myfirstmac.request.UserSearch;
import com.example.myfirstmac.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long create(UserCreate userCreate) {
        // userCreate -> Entity

//        User user = new User(userCreate.getUserId(), userCreate.getName(), userCreate.getAddress());

        User user = User.builder()
                .userId(userCreate.getUserId())
                .name(userCreate.getName())
                .address(userCreate.getAddress()).build();

        User savedUser = userRepository.save(user);
        Long id = savedUser.getId(); // 식별자 값 반환을 요구할 경우 사용


//        return savedUser;
        return id;
    }

    public UserResponse getUser(Long id) {

        System.out.println("id = " + id);
        System.out.println("now repo count = " + userRepository.count());
        User test = userRepository.findAll().get(0);
        System.out.println("repo print!");
        System.out.println(test.getId());
        System.out.println(test.getUserId());
        System.out.println(test.getName());

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));

        UserResponse userResponse = UserResponse.builder().id(user.getId()).name(user.getName()).userId(user.getUserId()).address(user.getAddress()).build();
        return userResponse;
    }


    public List<UserResponse> getList(UserSearch userSearch) {
//        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("id")));
//        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "id"));
//        return userRepository.findAll(pageable).stream()
//                .map(UserResponse::new)
//                .collect(Collectors.toList());

        return userRepository.getList(userSearch).stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }
}
