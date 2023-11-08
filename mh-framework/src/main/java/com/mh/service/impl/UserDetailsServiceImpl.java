package com.mh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mh.constants.SystemConstants;
import com.mh.domain.entity.LoginUser;
import com.mh.domain.entity.User;
import com.mh.mapper.MenuMapper;
import com.mh.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Author dmh
 * @Date 2023/7/18 10:03
 * @Version 1.0
 * @introduce 重新实现UserDetailsService接口，替代原有的内存操作，实现去数据库中查询
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    //这里只是，看这个登录的用户有没有，并没有经密码的校验
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(queryWrapper);
        //判断是否查到用户  如果没查到抛出异常 实际项目中报错用：账号或者密码错误，防止用户名被枚举出来
        if (Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误：用户名不存在");
        }
        //返回用户信息
        // TODO 查询权限信息封装(还没实现，已经实现)(只有后台用户有这个权限)
        if(user.getType().equals(SystemConstants.ADMAIN)){
            List<String> list = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user,list);
        }

        //这里使用了新的额LoginUser，所以需要实现UserDetails接口
        return new LoginUser(user,null);
    }
}
