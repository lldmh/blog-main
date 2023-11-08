package com.mh.service;

import com.mh.domain.ResponseResult;
import com.mh.domain.entity.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @Author dmh
 * @Date 2023/7/18 9:15
 * @Version 1.0
 * @introduce
 */

public interface BlogLoginService {
    //登录操作
    ResponseResult login(User user);
    //注销
    ResponseResult logout();
}
