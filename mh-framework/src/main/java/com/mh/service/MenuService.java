package com.mh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mh.domain.ResponseResult;
import com.mh.domain.dto.MenuDto;
import com.mh.domain.entity.Menu;
import com.mh.domain.vo.MenuVo;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-07-25 10:22:47
 */
public interface MenuService extends IService<Menu> {
    //通过当前用户ID查询用户的权限
    List<String> selectPermsByUserId(Long id);
    //查询menu 结果是tree的形式
    List<MenuVo> selectRouterMenuTreeById(Long userId);
    //查询菜单列表
    ResponseResult getMenuList(String status, String menuName);
    //添加菜单
    ResponseResult addMenu(MenuDto menuDto);
    //根据id查询菜单
    ResponseResult getMenuById(Long id);
    //更新菜单
    ResponseResult updateMenu(MenuDto menuDto);
    //删除菜单
    ResponseResult deleteMenu(Long id);
    //加载对应角色菜单列表树接口
    ResponseResult roleMenuTreeselect(Long id);
    //选择菜单树列表
    List<Menu> selectMenuList(Menu menu);
//    查询菜单树
    ResponseResult getMenuTree();
}

