package com.mh.Controller;

import com.mh.domain.ResponseResult;
import com.mh.domain.dto.AdminUserDto;
import com.mh.domain.dto.UpdateUserInfoRoleIdDto;
import com.mh.domain.dto.UserInfoDto;
import com.mh.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author dmh
 * @Date 2023/7/31 19:25
 * @Version 1.0
 * @introduce
 */
@RestController
@RequestMapping("/system/user")
public class AdminUserController {
    @Autowired
    private AdminUserService adminUserService;

    /**
     * 用户列表
     * @param pageNum
     * @param pageSize
     * @param userInfoDto
     * @return
     */
    @GetMapping("/list")
    public ResponseResult getUserInfoList(Integer pageNum, Integer pageSize, UserInfoDto userInfoDto){
        return adminUserService.getUserInfoList(pageNum,pageSize,userInfoDto);
    }

    /**
     * 添加用户
     *
     * @param adminUserDto 管理用户dto
     * @return {@link ResponseResult}
     */
    @PostMapping("")
    public ResponseResult addUser(@RequestBody AdminUserDto adminUserDto){
        return adminUserService.addUser(adminUserDto);
    }

    /**
     * 删除用户
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable Long id){
        return adminUserService.deleteUser(id);
    }

    /**
     * 通过id获取用户信息
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @GetMapping("/{id}")
    public ResponseResult getUserInfoById(@PathVariable Long id){
        return adminUserService.getUserInfoById(id);
    }

    /**
     * 更新用户信息
     *
     * @param updateUserInfoRoleIdDto 更新用户信息签证官
     * @return {@link ResponseResult}
     */
    @PutMapping
    public ResponseResult updateUserInfo(@RequestBody UpdateUserInfoRoleIdDto updateUserInfoRoleIdDto){
        return adminUserService.updateUserInfo(updateUserInfoRoleIdDto);
    }
}
