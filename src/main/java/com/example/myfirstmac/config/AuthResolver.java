package com.example.myfirstmac.config;

import com.example.myfirstmac.config.data.UserSession;
import com.example.myfirstmac.domain.session.Session;
import com.example.myfirstmac.exception.Unauthorized;
import com.example.myfirstmac.exception.UserNotFound;
import com.example.myfirstmac.repository.SessionRepository;
import io.jsonwebtoken.*;
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

//        log.info(">>>>> value === {}", appConfig);
        log.info(">>> jwt_key === {}", appConfig.jwtKey);

        // 여기왔다는 건 사용자가 JWT를 Authorization 헤더에 넣어서 인가가 필요한 url 로 요청을 보냈다는 말이다
        // JWT는 로그인 시에 발급된 것으로, 유저 아이디등의 정보를 담아서 생성해서 반환한 것.
        //

        List<Integer> list = new ArrayList<>();

        String jws = webRequest.getHeader("Authorization");

        if (jws == null || jws.equals("")) {
            throw new Unauthorized();
        }

        try {
            SecretKey decodedKey = Keys.hmacShaKeyFor(appConfig.getJwtKey());

            JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(decodedKey)
                    .build();

            Jws<Claims> claimsJws = jwtParser
                    .parseClaimsJws(jws);

            // 유저가 보낸 JWT 를 공유한 비밀키값으로 파싱하고 나면 조회가 가능하다.
            // JWT 파싱에 오류가 발생하지 않았다는 건 인증 Authentication 이 이뤄졌다는 말이다.
            // 지금 단계에선 인증이 이뤄지면 인가도 자동으로 이뤄지는 수준으로 한다.
            // 로그인이 정상적으로 이뤄지면 세션 객체 (논리적 연결을 유지하려는 객체)에다가 유저가 JWT에 담아보낸
            // 유저 ID를 담아서 반환한다.
            // 이는 인증이 진행되었음을 말한다.
            // 요약해보면 로그인 -> jwt 발급 -> 유저는 브라우저에 jwt 를 갖고 있다가 요청 시에 jwt 를 Authorization 헤더에 담아서 요청
            // 요청 시에 공유된 비밀키를 통해서 jwt 를 복호화한다. 복호화되면 인증이 되었다는 소리다.
            // 그렇게 복호화해서 얻은 userId를 사용할 수 있다.
            // 지금은 클라이언트 환경이 없으므로 유저마다 다르게 발급되는 jwt 를 귀찮지만 바꿔줘야 할 뿐이다.
            // 환경이 다 갖춰지면 유저는 요청하고 jwt 를 발급받고 이를 서버 측으로 요청만 하면 공유된 비밀키로 검증을 진행하면 된다.
            // 개별적 인가는 별개의 문제.

            String userId = claimsJws.getBody().getSubject();


            return new UserSession(Long.valueOf(userId));
        }
        catch (JwtException ex) {
            throw new Unauthorized();
        }
    }
}
