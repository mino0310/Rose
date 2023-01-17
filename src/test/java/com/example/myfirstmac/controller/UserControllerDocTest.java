package com.example.myfirstmac.controller;

import com.example.myfirstmac.domain.user.User;
import com.example.myfirstmac.repository.UserRepository;
import com.example.myfirstmac.request.UserCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.mino.com", uriPort = 443)
@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
public class UserControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("아무테스트")
    void test() throws Exception {
        // given
        User user = User.builder().userId("bewriter310").name("김민호").address("전농동").build();
        userRepository.save(user);


        this.mockMvc.perform(get("/users/{userId}", 1L).accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-inquiry",
                        pathParameters(
                                parameterWithName("userId").description("유저 ID")
                        )
                        , responseFields(
                                fieldWithPath("id").description("식별자"),
                                fieldWithPath("userId").description("유저 ID"),
                                fieldWithPath("name").description("유저 이름"),
                                fieldWithPath("address").description("유저 주소")
                        )
                ));

    }

    @Test
    @DisplayName("회원 등록")
    void test2() throws Exception {
        // given
        UserCreate userCreate = UserCreate.builder().userId("1").name("짱짱민호").address("전농동동").build();
        String json = objectMapper.writeValueAsString(userCreate);


        System.out.println("json = " + json);
        this.mockMvc.perform(post("/createUser")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-create",
                        requestFields(
                                fieldWithPath("userId").description("유저 ID"),
                                fieldWithPath("name").description("유저 이름").attributes(Attributes.key("constraint").value("이름을 입력해주세요")),
                                fieldWithPath("address").description("주소").optional()
                        )
                ));

    }


    @AfterEach
    void cleanData() {
        userRepository.deleteAll();
    }
}
