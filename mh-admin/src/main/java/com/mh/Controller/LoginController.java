package com.mh.Controller;

import com.mh.domain.ResponseResult;
import com.mh.domain.entity.LoginUser;
import com.mh.domain.entity.User;
import com.mh.domain.vo.AdminUserInfoVo;
import com.mh.domain.vo.MenuVo;
import com.mh.domain.vo.RoutersVo;
import com.mh.domain.vo.UserInfoVo;
import com.mh.enums.AppHttpCodeEnum;
import com.mh.exception.SystemException;
import com.mh.service.LoginService;
import com.mh.service.MenuService;
import com.mh.service.RoleService;
import com.mh.utils.BeanCopyUtils;
import com.mh.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author dmh
 * @Date 2023/7/24 15:49
 * @Version 1.0
 * @introduce
 */
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;


    //后台用户登录接口
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        System.out.println("我执行了这个了login");
        if(!StringUtils.hasText(user.getUserName())){
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }
    //获取信息接口
    @GetMapping("getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        System.out.println("我执行了这个了getInfo");
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //通过当前用户ID查询用户的权限
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        //获取用户的信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        //封装返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roleKeyList,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    //获取动态路由接口
    @GetMapping("getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        System.out.println("我执行了这个了getRouters");
        //获得当前用户的id
        Long userId = SecurityUtils.getUserId();
        //查询menu 结果是tree的形式
        List<MenuVo> menuVos = menuService.selectRouterMenuTreeById(userId);
        return ResponseResult.okResult(new RoutersVo(menuVos));
//        return null;
    }

    //注销登录接口
    @PostMapping("/user/logout")
    public ResponseResult logout(){
//        return ResponseResult.okResult();
        return loginService.logout();
    }
}