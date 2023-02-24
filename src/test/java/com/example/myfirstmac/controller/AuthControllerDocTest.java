package com.example.myfirstmac.controller;

import com.example.myfirstmac.config.AppConfig;
import com.example.myfirstmac.domain.session.Session;
import com.example.myfirstmac.domain.user.User;
import com.example.myfirstmac.exception.UserNotFound;
import com.example.myfirstmac.repository.SessionRepository;
import com.example.myfirstmac.repository.UserRepository;
import com.example.myfirstmac.request.Login;
import com.example.myfirstmac.request.Signup;
import com.example.myfirstmac.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jdk.internal.net.http.common.Log;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.RequestCookie;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.mino.com", uriPort = 443)
@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
class AuthControllerDocTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void clean() {
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("로그인 성공")
    void loginPass() throws Exception {
        // given
        userRepository.save(User.builder()
                .email("mino_test@naver.com")
                .name("minokim")
                .password("1234").build());
        Login login = Login.builder().name("minokim").email("mino_test@naver.com").password("1234").build();
        String jsonLogin = objectMapper.writeValueAsString(login);

        // expected
        this.mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLogin))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("로그인 성공 후 세션 1개 생성")
    @Transactional
    void loginAndGetSession() throws Exception {
        // given
        User loggedUser = userRepository.save(User.builder().email("mino_test@naver.com").name("minokim").password("1234").build());
        userRepository.save(loggedUser);

        Login login = Login.builder().name("minokim").email("mino_test@naver.com").password("1234").build();
        String jsonLogin = objectMapper.writeValueAsString(login);


        // expected
        this.mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLogin))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        User user = userRepository.findById(loggedUser.getId()).orElseThrow(UserNotFound::new);

        Assertions.assertEquals(1L, user.getSessions().size());
    }

/*
세션 방식에서 쿠키 방식으로 변환함에 따라 테스트 삭제.
    @Test
    @DisplayName("로그인 성공 후 세션 응답")
    void loginAndGetSessionObj() throws Exception {
        // given
        User loggedUser = userRepository.save(User.builder().email("mino_test@naver.com").name("minokim").password("1234").build());
        userRepository.save(loggedUser);

        Login login = Login.builder().name("minokim").email("mino_test@naver.com").password("1234").build();
        String jsonLogin = objectMapper.writeValueAsString(login);


        // expected
        this.mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLogin))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken", Matchers.notNullValue()));
    }
*/


    @Test
    @DisplayName("아이디와 비밀번호를 DB의 값과 같이 입력하면 로그인이 된다.")
    void signinTest() throws Exception {

        // given
        userRepository.save(User.builder().name("김민호").email("mino_0310@naver.com").password("mino93").build());

        // HTTP 요청
        Login login = Login.builder().name("김민호").email("mino_0310@naver.com").password("mino93").build();

        String jsonLogin = objectMapper.writeValueAsString(login);

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLogin))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentation.document("login",
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("name").description("이름"),
                                PayloadDocumentation.fieldWithPath("email").description("이메일"),
                                PayloadDocumentation.fieldWithPath("password").description("비밀번호")
                        )
//                        PayloadDocumentation.responseFields(),
//                        RequestDocumentation.requestParameters()
                ));
        // 로그인 시도

        // expected
        // 성공

    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지에 접속한다 /foo")
    void loginAndConnectAuthPage() throws Exception {

        // given
        // 회원가입
        User loggedUser = userRepository.save(User.builder().name("미노").email("mino@naver.com").password("1234").build());

        // 로그인 정보 전달
        Login logInformation = Login.builder().name("미노").email("mino@naver.com").password("1234").build();
        String json = this.objectMapper.writeValueAsString(logInformation);

        // 로그인
        Long loggedUserId = authService.signin(logInformation);

        // 로그인에 사용한 토큰값
        SecretKey secretKey = Keys.hmacShaKeyFor(appConfig.getJwtKey());
        String jws = Jwts.builder()
                .setSubject(String.valueOf(loggedUserId))
                .signWith(secretKey)
                .setIssuedAt(new Date())
                .compact();

        // expected
        this.mockMvc.perform(MockMvcRequestBuilders.get("/foo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", jws))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    // JWT로 변경하며 삭제
/*
    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다.")
    void invalidSessionAccessDenied() throws Exception{

        // given
        // 회원가입
        User user = User.builder().name("mimi").email("mimi@naver.com").password("mimi").build();

        // 이 과정에서 세션값이 발급됨. 서비스 계층까지 넘어갈 수 없으므로 서비스 계층에서 생성하는 세션을 생성해준다.
        // 세션이 생성되었다는 것은 유저가 회원가입 후 로그인을 한 차례 했다는 상황을 만들기 위해서임.
        Session session = user.addSession();
        // 세션값이 생성되어 있는 유저를 DB에 저장한다. 세션값이 있으므로 세션 테이블에 세션값이 저장된다.
        // DB에 Insert 까지 일어남으로, 회원가입과 로그인이 이뤄진 상황과 같아진다.
        User savedUser = userRepository.save(user);

        // accessToken value
        String accessToken = session.getAccessToken();

        // 임의의 세션값 UUID 로 생성 (테스트 용도)
        String randomAccessToken = UUID.randomUUID().toString();

        // 쿠키를 생성해서 요청에 넣어줘야 한다.

        // expected
        // 유효하지 않은 세션값으로 유효한 세션값이 필요한 페이지에 접근
        this.mockMvc.perform(MockMvcRequestBuilders.get("/foo")
                        .cookie(new Cookie("SESSION", accessToken)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

*/

    @Test
    @DisplayName("회원가입 성공")
    void signup() throws Exception{

        // given
        Signup signup = Signup.builder()
                .email("mino0310@naver.com")
                .name("mino")
                .password("1234")
                .build();

        String signupJson = objectMapper.writeValueAsString(signup);

        // expected

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentation.document("signup",
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("name").description("이름"),
                                PayloadDocumentation.fieldWithPath("email").description("이메일"),
                                PayloadDocumentation.fieldWithPath("password").description("비밀번호")
                        )));
    }

    @Test
    @DisplayName("이메일이 중복될 경우 가입 실패")
    void duplicatedEmail() throws Exception {

        // given
        // 기존 회원 가입
        User user = userRepository.save(User.builder().email("mino0310@naver.com")
                .name("aaaa")
                .password("123333")
                .build());

        // 회원 가입 객체
        Signup signup = Signup.builder()
                .email("mino0310@naver.com")
                .name("mino")
                .password("1234")
                .build();

        String signupJson = objectMapper.writeValueAsString(signup);

        // expected


        this.mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}