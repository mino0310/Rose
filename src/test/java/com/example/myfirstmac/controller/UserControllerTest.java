package com.example.myfirstmac.controller;

import com.example.myfirstmac.domain.user.User;
import com.example.myfirstmac.repository.UserRepository;
import com.example.myfirstmac.request.UserCreate;
import com.example.myfirstmac.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .andExpect(jsonPath("$.code").value("400"))
                // 여기서 하드코딩되어 있어서 개선이 필요함. 추가로 알아볼 것.
                .andExpect(jsonPath("$.validation.userId").value("아이디를 입력해주세요"))
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
                        get("/user/{userId}", user.getId())
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

    @AfterEach
    void cleanData() {
        userRepository.deleteAll();
    }
}