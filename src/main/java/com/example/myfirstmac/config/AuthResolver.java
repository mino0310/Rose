package com.example.myfirstmac.config;

import com.example.myfirstmac.config.data.UserSession;
import com.example.myfirstmac.domain.session.Session;
import com.example.myfirstmac.exception.Unauthorized;
import com.example.myfirstmac.exception.UserNotFound;
import com.example.myfirstmac.repository.SessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class  AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;
    private final AppConfig appConfig;

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
//        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        // JWT 를 사용하면 DB 를 이용할 필요도 없어진다. JWT로 인증을 진행하기 때문.

        log.info(">>>>> value === {}", appConfig);

        List<Integer> list = new ArrayList<>();

        String jws = webRequest.getHeader("Authorization");

        if (jws == null || jws.equals("")) {
            throw new Unauthorized();
        }

        try {
            SecretKey decodedKey = Keys.hmacShaKeyFor(appConfig.getJwtKey());

            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(decodedKey)
                    .build()
                    .parseClaimsJws(jws);

            String userId = claimsJws.getBody().getSubject();

            return new UserSession(Long.valueOf(userId));
        }
        catch (JwtException ex) {
            throw new Unauthorized();
        }
    }
}
