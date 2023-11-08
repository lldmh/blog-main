package com.mh.service;

import com.mh.domain.ResponseResult;
import com.mh.domain.dto.AdminUserDto;
import com.mh.domain.dto.UpdateUserInfoRoleIdDto;
import com.mh.domain.dto.UserInfoDto;

/**
 * @Author dmh
 * @Date 2023/7/31 19:32
 * @Version 1.0
 * @introduce
 */
public interface AdminUserService {
//    获得全部列表
    ResponseResult getUserInfoList(Integer pageNum, Integer pageSize, UserInfoDto userInfoDto);
//    添加用户
    ResponseResult addUser(AdminUserDto adminUserDto);
//    删除用户
    ResponseResult deleteUser(Long id);
//    通过id获取用户信息
    ResponseResult getUserInfoById(Long id);
//    更新用户信息
    ResponseResult updateUserInfo(UpdateUserInfoRoleIdDto updateUserInfoRoleIdDto);

}
