package com.example.myfirstmac.config;

import com.example.myfirstmac.exception.Unauthorized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 규모가 커졌을 땐 패스를 하나씩 추가하는 것이 번거롭고 복잡성이 증가해서 ArgumentResolver 로 대체
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      log.info(">> preHandle");

        String accessToken = request.getParameter("accessToken");
//        String accessToken = request.getHeader("accessToken");
        if (accessToken != null && !accessToken.equals("")) {
            request.setAttribute("userName", accessToken);
            return true;
        }
        throw new Unauthorized();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
