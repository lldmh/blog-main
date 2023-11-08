package com.mh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mh.domain.entity.UserRole;

import java.util.List;


/**
 * 用户和角色关联表(UserRole)表服务接口
 *
 * @author makejava
 * @since 2023-07-31 20:07:37
 */
public interface UserRoleService extends IService<UserRole> {

    List<Long> getUserRoleById(Long id);
}

