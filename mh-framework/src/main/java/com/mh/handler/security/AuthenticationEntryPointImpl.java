package com.mh.handler.security;

import com.alibaba.fastjson.JSON;
import com.mh.domain.ResponseResult;
import com.mh.enums.AppHttpCodeEnum;
import com.mh.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author dmh
 * @Date 2023/7/19 20:46
 * @Version 1.0
 * @introduce  //认证失败处理器
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //打印异常信息
        authException.printStackTrace();
        //InsufficientAuthenticationException
        //BadCredentialsException
        //响应给前端
        ResponseResult result = null;
        if (authException instanceof BadCredentialsException){   //认证错误异常，用户名或密码错误
            ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(),authException.getMessage());
        }else if (authException instanceof InsufficientAuthenticationException) { //登录异常
            result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }else {
            result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"认证或授权失败");   //因为不想让用户知道是认证失败还是授权失败
        }

        //转换成前端所需要的
        WebUtils.renderString(response, JSON.toJSONString(result));
    }
}
