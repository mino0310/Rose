package com.example.myfirstmac.service;

import com.example.myfirstmac.domain.user.User;
import com.example.myfirstmac.repository.UserRepository;
import com.example.myfirstmac.request.UserCreate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clean(){
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("유저 생성")
    void test1() {

        // given

        UserCreate userCreate = UserCreate.builder()
                .userId("bewriter310")
                .name("김민호")
                .address("전농동")
                .build();


        // when
        userService.create(userCreate);

        // then
        Assertions.assertEquals(1L, userRepository.count());

        User user = userRepository.findAll().get(0);
        assertEquals("bewriter310", user.getUserId());
        assertEquals("김민호", user.getName());
        assertEquals("전농동", user.getAddress());

    }

    @Test
    @DisplayName("회원 1개 조회")
    void test2() {

        // give
        User user = User.builder().userId("bewriter310").name("김민호").address("전농동").build();

        userRepository.save(user);

        Long userId = 1L;

        // when
        User retrievedUser = userService.getUser(userId);

        // then
        Assertions.assertNotNull(retrievedUser);
        assertEquals("bewriter310", retrievedUser.getUserId());
        assertEquals("김민호", retrievedUser.getName());
        assertEquals("전농동", retrievedUser.getAddress());

    }
}