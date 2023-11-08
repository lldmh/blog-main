package com.mh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mh.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-07-25 10:23:00
 */
public interface RoleMapper extends BaseMapper<Role> {
    //根据用户id查询角色信息
    List<String> selectRoleKeyByUserId(Long id);
}

