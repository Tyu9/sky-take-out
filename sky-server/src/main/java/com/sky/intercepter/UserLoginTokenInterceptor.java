package com.sky.intercepter;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author：tantantan
 * @Package：com.sky.intercepter
 * @Project：sky-take-out
 * @name：AdminLoginToken
 * @Date：2024/5/19 14:21
 * @Filename：AdminLoginToken
 */
@Slf4j
@Component
public class UserLoginTokenInterceptor implements HandlerInterceptor {
    @Autowired
    JwtProperties jwtProperties;
    //preHandle方法控制请求的拦截和放行，返回true表示放行，返回false表示拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截器拦截成功,{}",request.getRequestURI().toString());
        //1、获取请求中的token
        String token = request.getHeader(jwtProperties.getUserTokenName());
        //2、判断令牌是否存在
        if(!StringUtils.hasLength(token)){
            //不存在则返回401
            log.info("无操作权限");
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            return false;
        }
        //3、存在则校验令牌是否有效
        try {
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            BaseContext.setCurrentId(userId);
            log.info("令牌正确,放行");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("令牌错误,响应401");
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

    //postHandle方法主要是做请求经过后台业务处理后，返回给前台页面之前调用操作
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清除线程变量
       BaseContext.removeCurrentId();
    }
}
