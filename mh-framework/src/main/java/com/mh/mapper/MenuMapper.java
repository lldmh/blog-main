package com.mh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mh.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-07-25 10:22:47
 */
public interface MenuMapper extends BaseMapper<Menu> {
    //通过当前用户ID查询用户的权限
    List<String> selectPermsByUserId(Long id);
//    获取所有符合要求的Menu
    List<Menu> selectAllRouterMenu();
//    获取当前用户所具有的Menu
    List<Menu> selectRouterMenuTreeByUserId(Long userId);
//    选择菜单通过角色id列表
    List<Long> selectMenuListByRoleId(Long roleId);
}

