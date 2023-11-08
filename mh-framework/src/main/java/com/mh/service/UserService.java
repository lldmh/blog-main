package com.mh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mh.domain.ResponseResult;
import com.mh.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-07-20 10:37:50
 */
public interface UserService extends IService<User> {

    //查询当前用户的信息
    ResponseResult userInfo();
    //编辑个人资料
    ResponseResult updateUserInfo(User user);
    //注册
    ResponseResult register(User user);
}

