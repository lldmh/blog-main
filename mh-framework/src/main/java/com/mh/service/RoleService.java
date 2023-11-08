package com.mh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mh.domain.ResponseResult;
import com.mh.domain.dto.RoleDto;
import com.mh.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-07-25 10:23:00
 */
public interface RoleService extends IService<Role> {
    //根据用户id查询角色信息
    List<String> selectRoleKeyByUserId(Long id);
    //角色列表
    ResponseResult getRoleList(Integer pageNum, Integer pageSize, String roleName);
    //改变角色状态
    ResponseResult changeStatus(RoleDto roleDto);
    //添加角色
    ResponseResult addRole(RoleDto roleDto);
    //查询角色
    ResponseResult getRoleById(Long id);
    //修改角色
    ResponseResult updateRoleInfo(RoleDto roleDto);
    //删除角色
    ResponseResult deleteRoleById(Long id);
    //查询角色列表
    ResponseResult getListAllRole();
    //查询所有角色的列表
    List<Role> getAllRoleList();
}

