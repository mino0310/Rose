package com.example.myfirstmac.config;

import com.example.myfirstmac.config.data.UserSession;
import com.example.myfirstmac.domain.session.Session;
import com.example.myfirstmac.exception.Unauthorized;
import com.example.myfirstmac.exception.UserNotFound;
import com.example.myfirstmac.repository.SessionRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 토큰 값을 parameter 로 가져오면 다른 값들과 충돌을 일으킬 수 있으므로 헤더로 값을 가져오는 것이 좋다
//        String accessToken = webRequest.getParameter("accessToken");
//        String accessToken = webRequest.getHeader("Authorization");

        // 이전엔 헤더값을 Authorization으로 직접지정해줬지만 이제는 쿠키를 사용한다
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        if (request == null) {
            log.error ("ServletRequest Error!");
            throw new Unauthorized();
        }

        Cookie[] cookies = request.getCookies();
        if (cookies.length == 0) {
            log.error("cookie doesn't exist!");
            throw new Unauthorized();
        }

        String accessToken = cookies[0].getValue();


        if (accessToken == null || accessToken.equals("")) {
            throw new Unauthorized();
        }

        // 데이터베이스 사용자 확인 작업
        Session session = sessionRepository.findByAccessToken(accessToken).orElseThrow(Unauthorized::new);



        // lambda 로 한 다음 new 객체 하면은 필드에 자동으로 값을 넣어주는 걸로 바꿔도 될 듯. 필드가 많아지면.
        return UserSession.builder().id(session.getUser().getId()).build();
    }
}
