package com.mh.controller;

import com.mh.domain.ResponseResult;
import com.mh.domain.entity.User;
import com.mh.service.BlogLoginService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author dmh
 * @Date 2023/7/17 21:22
 * @Version 1.0
 * @introduce  登录接口
 */
@RestController
@Api(tags = "博客登录",description = "博客登录相关接口")
public class BlogLoginController {
    @Autowired
    private BlogLoginService blogLoginService;

    //登录操作
    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        return blogLoginService.login(user);
    }

    //注销
    @PostMapping("/logout")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }
}
