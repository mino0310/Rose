package com.example.myfirstmac.controller;

import com.example.myfirstmac.domain.user.User;
import com.example.myfirstmac.request.UserCreate;
import com.example.myfirstmac.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/createUser")
    public void createUser(@RequestBody @Valid UserCreate request) throws Exception {
        userService.create(request);
        // post 요청 시엔 별다른 응답을 내려주지 않는다.
        // TEST 시에는 빈 값으로 처리해야 함.

        // post 요청은 200 이나 201 을 사용한다.
        // Case1. 저장한 데이터 Entity -> response 로 응답
        // Case2. 저장한 데이터의 Primary_id -> response 로 응답
        // Case3. 응답 필요없음 -> 클라이언트에서 모든 데이터 Context를 관리함.

//        return Map.of("userId", userId);
    }

    @GetMapping("/user/{userId}")
    public User getUser(@PathVariable(name = "userId") Long id) {

        User user = userService.getUser(id);

        return user;

    }
}
