package com.example.myfirstmac.controller;

import com.example.myfirstmac.domain.user.User;
import com.example.myfirstmac.repository.UserRepository;
import com.example.myfirstmac.request.UserCreate;
import com.example.myfirstmac.request.UserEdit;
import com.example.myfirstmac.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//MVC Test 에 필요한 Mock 객체를 주입받기 위한 어노테이션
// 간단한 웹 레이어의 컨트롤러 테스트에는 적합
// @WebMvcTest
// 이것과 springBootTest 어노테이션은 같이 사용할 수 없다. 초기 설정을 다르게 해야함.
// 그럴 경우 다음 어노테이션을 사용하면 된다.
@AutoConfigureMockMvc
// 서비스도 테스트하고 레파지토리도 테스트하는 전반적인 스프링을 이용해야 한다면 스프링을 띄워야 하므로 다음 어노테이션을 사용한다.
@SpringBootTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 테스트 케이스에서는 필드주입을 받아서 사용하곤 함.
    @Autowired
    private UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    @DisplayName("createUser 요청 시 유저를 생성하고 그 정보를 출력한다.")
    void test1() throws Exception {

        //given
//        UserCreate userCreate = new UserCreate("bewriter310", "김민호", "전농동");
        UserCreate userCreate = UserCreate.builder()
                .userId("bewriter310")
                .name("김민호")
                .address("전농동")
                .build();

        String json = objectMapper.writeValueAsString(userCreate);

        mockMvc.perform(
                        post("/createUser")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print()); // 요청에 대한 정보를 출력한다.
    }

    @Test
    @DisplayName("createUser 요청 시 id값은 필수다.")
    void test2() throws Exception {


        //given
        UserCreate userCreate = UserCreate.builder()
                .name("김민호")
                .address("전농동")
                .build();

        String json = objectMapper.writeValueAsString(userCreate);

        mockMvc.perform(
                        post("/createUser")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.code").value("400"))
                // 여기서 하드코딩되어 있어서 개선이 필요함. 추가로 알아볼 것.
//                .andExpect(jsonPath("$.validation.userId").value("아이디를 입력해주세요"))
                .andDo(print()); // 요청에 대한 정보를 출력한다.
    }

    @Test
    @DisplayName("createUser 요청 시 DB에 값이 저장된다.")
    void test3() throws Exception {

        //given
        UserCreate userCreate = UserCreate.builder()
                .userId("bewriter310")
                .name("김민호")
                .address("전농동")
                .build();

        String json = objectMapper.writeValueAsString(userCreate);

        //when
        mockMvc.perform(
                        post("/createUser")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print()); // 요청에 대한 정보를 출력한다.

        //then
        assertEquals(1L, userRepository.count());

        User user = userRepository.findAll().get(0);
        assertEquals("bewriter310", user.getUserId());
        assertEquals("김민호", user.getName());
        assertEquals("전농동", user.getAddress());

    }

    @Test
    @DisplayName("유저 1개 조회")
    void test4() throws Exception {

        // given
        User user = User.builder().userId("bewriter310").name("김민호").address("전농동").build();
        userRepository.save(user);


        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(
                        get("/users/{userId}", user.getId())
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.address").value(user.getAddress()))
                .andDo(print()); // 요청에 대한 정보를 출력한다.
        // when
        // then
    }

    @Test
    @DisplayName("유저 여러개 조회")
    void test5() throws Exception {

        // given
        List<User> users = IntStream.range(0, 30)
                .mapToObj(i -> User.builder()
                        .userId("testId" + i)
                        .name("testName" + i)
                        .address("testAdd" + i)
                        .build()
                )
                .collect(Collectors.toList());

        userRepository.saveAll(users);



        // Service 에서는 pageable 객체를 만들면서 페이징에 대한 정보를 넘겼으나
        // Controller 는 HTTP 통신이나 웹 관련 처리만 하므로 이를 통해 페이지에 대한 정보를 넘겨줘야 한다.
        // 프론트 쪽에서 10개, 20개, 50개 보기등의 기능이 필요로 하면 파라미터로 넘겨서 처리해줘도 좋으나 보통은 서버 측에서 내려주는 대로 뿌리는 경우가 많다.
        mockMvc.perform(
                        get("/users?page=1&size=10")
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$[0].userId").value("testId29"))
                .andExpect(jsonPath("$[0].name").value("testName29"))
                .andExpect(jsonPath("$[0].address").value("testAdd29"))
                .andDo(print()); // 요청에 대한 정보를 출력한다.
        // when
        // then
    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다.")
    void test6() throws Exception {

        // given
        List<User> users = IntStream.range(0, 30)
                .mapToObj(i -> User.builder()
                        .userId("testId" + i)
                        .name("testName" + i)
                        .address("testAdd" + i)
                        .build()
                )
                .collect(Collectors.toList());

        userRepository.saveAll(users);



        // Service 에서는 pageable 객체를 만들면서 페이징에 대한 정보를 넘겼으나
        // Controller 는 HTTP 통신이나 웹 관련 처리만 하므로 이를 통해 페이지에 대한 정보를 넘겨줘야 한다.
        // 프론트 쪽에서 10개, 20개, 50개 보기등의 기능이 필요로 하면 파라미터로 넘겨서 처리해줘도 좋으나 보통은 서버 측에서 내려주는 대로 뿌리는 경우가 많다.
        mockMvc.perform(
                        get("/users?page=1&size=10")
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$[0].userId").value("testId29"))
                .andExpect(jsonPath("$[0].name").value("testName29"))
                .andExpect(jsonPath("$[0].address").value("testAdd29"))
                .andDo(print()); // 요청에 대한 정보를 출력한다.
        // when
        // then
    }

    @Test
    @DisplayName("회원 이름 수정")
    void test7() throws Exception {

        // given
        User user = User.builder().userId("testId")
                .name("testName")
                .address("testAdd")
                .build();
        userRepository.save(user);

        UserEdit userEdit = UserEdit.builder()
                .name("mino")
                .address("testAdd")
                .build();

        // expected
        mockMvc.perform(
                        patch("/users/{userId}", user.getId())
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userEdit))
                )
                .andExpect(status().isOk())
                .andDo(print()); // 요청에 대한 정보를 출력한다.
    }


    @Test
    @DisplayName("회원 삭제")
    void test8() throws Exception {

        // given
        User user = User.builder().userId("testId")
                .name("testName")
                .address("testAdd")
                .build();
        userRepository.save(user);

        // expected
        mockMvc.perform(
                        delete("/users/{userId}", user.getId())
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print()); // 요청에 대한 정보를 출력한다.
    }

    @Test
    @DisplayName("존재하지 않는 회원 조회")
    void test9() throws Exception {

        mockMvc.perform(
                        delete("/users/{userId}", 1L)
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andDo(print()); // 요청에 대한 정보를 출력한다.
    }

    @Test
    @DisplayName("존재하지 않는 회원 수정")
    void test10() throws Exception {

        User user = User.builder().userId("testId")
                .name("testName")
                .address("testAdd")
                .build();
        userRepository.save(user);

        UserEdit userEdit = UserEdit.builder()
                .name("mino")
                .address("testAdd")
                .build();

        mockMvc.perform(
                        patch("/users/{userId}", user.getId() + 1)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userEdit))
                )
                .andExpect(status().isNotFound())
                .andDo(print()); // 요청에 대한 정보를
    }

    @Test
    @DisplayName("회원 생성 시 이름에 '욕설'이 들어갈 수는 없다.")
    void test11() throws Exception {

        //given
        UserCreate userCreate = UserCreate.builder()
                .userId("bewriter310")
                .name("김민욕설호")
                .address("전농동")
                .build();

        String json = objectMapper.writeValueAsString(userCreate);

        //when
        mockMvc.perform(
                        post("/createUser")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print()); // 요청에 대한 정보를 출력한다.

        //then

    }


    @AfterEach
    void cleanData() {
        userRepository.deleteAll();
    }
}