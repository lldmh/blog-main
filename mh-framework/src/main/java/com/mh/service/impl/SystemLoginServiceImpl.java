package com.mh.service.impl;

import com.mh.domain.ResponseResult;
import com.mh.domain.entity.LoginUser;
import com.mh.domain.entity.User;
import com.mh.domain.vo.BlogUserLoginVo;
import com.mh.domain.vo.UserInfoVo;
import com.mh.service.BlogLoginService;
import com.mh.service.LoginService;
import com.mh.utils.BeanCopyUtils;
import com.mh.utils.JwtUtil;
import com.mh.utils.RedisCache;
import com.mh.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author dmh
 * @Date 2023/7/18 9:17
 * @Version 1.0
 * @introduce 后台登录
 */
@Service
public class SystemLoginServiceImpl implements LoginService {
    //注入登录认证mapper
    //这里使用authenticationManager，需要进行配置SecurityConfig
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    //登录操作
    @Override
    public ResponseResult login(User user) {
        //认证用的token（这里会调用封装好的UserDetailService的接口，所以需要实现这个接口）
            // authentication这里面就有LoginUser的信息了
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        //判断认证是否通过
        if (Objects.isNull(authentication)){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userid 生成token
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();//获得UserDetailServiceImpl返回的主体（可以打断点，看那个方法返回值类型是LoginUser类型）
        String userId = loginUser.getUser().getId().toString();
        String jwt  = JwtUtil.createJWT(userId);  //对UserId进行加密  //jwt就是token
        //把用户信息存入redis（将整个user放进去）
        redisCache.setCacheObject("login:"+userId, loginUser);

        //把token封装 返回
        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);

        //封装一下
        return ResponseResult.okResult(map);
    }

    //注销
    @Override
    public ResponseResult logout() {
        //获取token 解析获取userid（同一个线程，并不会认证信息乱掉，线程隔离）
        System.out.println("执行了logout");
        Long userId = SecurityUtils.getUserId();
        //删除redis中的用户信息
        redisCache.deleteObject("login:"+userId);
        return ResponseResult.okResult();
    }
}
