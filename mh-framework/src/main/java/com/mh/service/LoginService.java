package com.mh.service;

import com.mh.domain.ResponseResult;
import com.mh.domain.entity.User;

/**
 * @Author dmh
 * @Date 2023/7/18 9:15
 * @Version 1.0
 * @introduce  后台登录
 */

public interface LoginService {
    //登录操作
    ResponseResult login(User user);
    //注销
    ResponseResult logout();
}
