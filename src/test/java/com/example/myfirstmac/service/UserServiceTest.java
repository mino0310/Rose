package com.example.myfirstmac.service;

import com.example.myfirstmac.domain.user.User;
import com.example.myfirstmac.repository.UserRepository;
import com.example.myfirstmac.request.UserCreate;
import com.example.myfirstmac.request.UserSearch;
import com.example.myfirstmac.response.UserResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        UserCreate user = UserCreate.builder().userId("bewriter310").name("김민호").address("전농동").build();

        Long id = userService.create(user);

        Long userId = id;

        // when
        UserResponse response = userService.getUser(userId);

        // then
        Assertions.assertNotNull(response);
        assertEquals("bewriter310", response.getUserId());
        assertEquals("김민호", response.getName());
        assertEquals("전농동", response.getAddress());

    }

    @Test
    @DisplayName("회원 여러개 조회")
    void test3() {

        // give
        List<User> savedUser = IntStream.range(0, 30)
                .mapToObj(i -> User.builder()
                        .userId("testId" + i)
                        .name("testName" + i)
                        .address("testAdd" + i)
                        .build()
                )
                .collect(Collectors.toList());
        userRepository.saveAll(savedUser);


        // 수동으로 만들어주고 이를 일단 넘겨주면 application.properties 에 있는 옵션값을 읽어서 첫번째 페이지의 인덱스를 설정해준다.
        // 이렇게 전달하면 0 인덱스부터 5개를 가져오는 건데 옵션값의 영향으로 0 페이지를 1페이지랑 매칭시켜준다.
//        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");

        UserSearch userSearch = UserSearch.builder()
                .page(1)
                .build();

        // when
        List<UserResponse> users = userService.getList(userSearch);

        // then
        assertEquals(10L, users.size());
        assertEquals("testId9", users.get(0).getUserId());
        assertEquals("testName9", users.get(0).getName());
        assertEquals("testAdd9", users.get(0).getAddress());
    }


}