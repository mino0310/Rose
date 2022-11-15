package com.example.myfirstmac.controller;

import com.example.myfirstmac.domain.user.User;
import com.example.myfirstmac.exception.InvalidRequest;
import com.example.myfirstmac.request.UserCreate;
import com.example.myfirstmac.request.UserEdit;
import com.example.myfirstmac.request.UserSearch;
import com.example.myfirstmac.response.UserResponse;
import com.example.myfirstmac.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/createUser")
    public void createUser(@RequestBody @Valid UserCreate request) throws Exception {

        if (request.getName().contains("욕설")) {
            throw new InvalidRequest();
        }
        userService.create(request);
        // post 요청 시엔 별다른 응답을 내려주지 않는다.
        // TEST 시에는 빈 값으로 처리해야 함.

        // post 요청은 200 이나 201 을 사용한다.
        // Case1. 저장한 데이터 Entity -> response 로 응답
        // Case2. 저장한 데이터의 Primary_id -> response 로 응답
        // Case3. 응답 필요없음 -> 클라이언트에서 모든 데이터 Context를 관리함.

//        return Map.of("userId", userId);
    }

    @GetMapping("/users/{userId}")
    public UserResponse getUser(@PathVariable(name = "userId") Long id) {
        return userService.getUser(id);

    }


    // 컨트롤러에서 받은 페이징 관련 값을 Pageable 로 받아도 되나 이거는 페이지관련 정보만 받게 된다.
    // 추후 소팅 기타 등등의 정보를 받아야 할 수 있으므로 별도의 클래스를 생성해서 정보를 저장해서 사용하는 것이 좋다.

    @GetMapping("/users")
    public List<UserResponse> getUserList(@ModelAttribute UserSearch userSearch) {
        // page 값을 직접 받아서 Service로 넘기면 spring.data.web.pageable.one-indexed-parameters=true 옵션이 동작하지 않음.
        // @PageableDefault 어노테이션을 사용해 기본값을 적용시켜버릴 수도 있음.
        // 직접 입력을 받으려면 해당 어노테이션을 제거해야 한다.

        return userService.getList(userSearch);
    }

    @PatchMapping("/users/{userId}")
    public void editUser(@PathVariable("userId") Long id, @RequestBody @Valid UserEdit request) {
        userService.edit(id, request);
    }

    @DeleteMapping("/users/{userId}")
    public void delete(@PathVariable("userId") Long id) {
        userService.delete(id);
    }
}
