package com.mh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mh.domain.entity.RoleMenu;

import java.util.List;


/**
 * 角色和菜单关联表(RoleMenu)表服务接口
 *
 * @author makejava
 * @since 2023-07-31 17:39:57
 */
public interface RoleMenuService extends IService<RoleMenu> {
    //查询当前角色的menuIds列表
    List<Long> getRoleMenuIdsById(Long id);
}

