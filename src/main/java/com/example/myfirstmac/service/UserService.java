package com.example.myfirstmac.service;


import com.example.myfirstmac.domain.user.User;
import com.example.myfirstmac.repository.UserRepository;
import com.example.myfirstmac.request.UserCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void create(UserCreate userCreate) {
        // userCreate -> Entity

//        User user = new User(userCreate.getUserId(), userCreate.getName(), userCreate.getAddress());

        User user = User.builder()
                .userId(userCreate.getUserId())
                .name(userCreate.getName())
                .address(userCreate.getAddress()).build();

        User savedUser = userRepository.save(user);
        Long id = savedUser.getId(); // 식별자 값 반환을 요구할 경우 사용

//        return savedUser;
//        return id;
    }

    public User getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));

        return user;
    }
}
