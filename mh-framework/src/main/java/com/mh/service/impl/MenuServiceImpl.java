package com.mh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mh.constants.SystemConstants;
import com.mh.domain.ResponseResult;
import com.mh.domain.dto.MenuDto;
import com.mh.domain.entity.Menu;
import com.mh.domain.vo.*;
import com.mh.enums.AppHttpCodeEnum;
import com.mh.exception.SystemException;
import com.mh.mapper.MenuMapper;
import com.mh.service.MenuService;
import com.mh.utils.BeanCopyUtils;
import com.mh.utils.SecurityUtils;
import com.mh.utils.SystemConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.print.DocFlavor;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-07-25 10:22:47
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;
    @Lazy
    @Autowired
    private MenuService menuService;

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有的权限
        if (SecurityUtils.isAdmin()){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            //利用数据流的方式取出menu对象中的具体权限
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //否则返回所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    //查询menu 结果是tree的形式
    @Override
    public List<MenuVo> selectRouterMenuTreeById(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<MenuVo> menuVos = null;
        List<Menu> menus = null;
        //判断是不是管理员
        if (SecurityUtils.isAdmin()){
            //如果是 获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else {
            //否则  获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        //bean拷贝
        menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);

        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<MenuVo> menuVoTree = builderMenuTree(menuVos,0L);

        return menuVoTree;
    }



    //菜单树
    private List<MenuVo> builderMenuTree(List<MenuVo> menuVos, long parentId) {
        List<MenuVo> menuVoTree = menuVos.stream()
                .filter(menuVo -> menuVo.getParentId().equals(parentId))
                .map(menuVo -> menuVo.setChildren(getChildren(menuVo, menuVos)))  //获取传入参数的子menuVo,getChildren(menuVo, menuVos)这里返回的是对象，需要链式编程
                .collect(Collectors.toList());
        return menuVoTree;
    }
    /**
     * 获取存入参数的 子Menu集合
     * @param menuVo
     * @param menuVos
     * @return
     * 从menus中找到menu的子menu
     */
    private List<MenuVo> getChildren(MenuVo menuVo, List<MenuVo> menuVos) {
        List<MenuVo> childrenList = menuVos.stream()
                .filter(m -> m.getParentId().equals(menuVo.getId()))   //过滤
                .map(m->m.setChildren(getChildren(m,menuVos)))    //过滤后的进行map转换，递归（三层及以上菜单）
                .collect(Collectors.toList());
        return childrenList;
    }

    //查询菜单列表
    @Override
    public ResponseResult getMenuList(String status, String menuName) {
        //查询条件
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.like(StringUtils.hasText(menuName), Menu::getMenuName, menuName);
//        queryWrapper.eq(Menu::getStatus, status);
        queryWrapper.eq(StringUtils.hasText(status), Menu::getStatus, status)
                .like(StringUtils.hasText(menuName), Menu::getMenuName, menuName)
                .orderByAsc(Menu::getParentId, Menu::getOrderNum);
            //菜单要按照父菜单id和orderNum进行排序
//        queryWrapper.orderByAsc(Menu::getParentId, Menu::getOrderNum);

        List<Menu> menus = list(queryWrapper);
        //将查询结果转化成MenuTwoVo
        List<MenuTwoVo> menuTwoVos = BeanCopyUtils.copyBeanList(menus, MenuTwoVo.class);
        return ResponseResult.okResult(menuTwoVos);
    }

    //添加菜单
    @Override
    public ResponseResult addMenu(MenuDto menuDto) {
        //看有没有这个目录
        Menu menu = BeanCopyUtils.copyBean(menuDto, Menu.class);
        //根据MenuName判断当前是否存在menu
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getMenuName, menu.getMenuName());
        Menu oneMenu = getOne(queryWrapper);
        //判断oneMenu有没有
        if (!Objects.isNull(oneMenu)){
            return ResponseResult.errorResult(AppHttpCodeEnum.ADD_MENU_FAIL);
        }
        //存储
        save(menu);
        return ResponseResult.okResult();
    }

    //根据id查询菜单
    @Override
    public ResponseResult getMenuById(Long id) {
        //查询条件
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getId, id);
        Menu menu = getOne(queryWrapper);
        //将menu转换成MenuThreeVo
        MenuThreeVo menuThreeVo = BeanCopyUtils.copyBean(menu, MenuThreeVo.class);
        //返回
        return ResponseResult.okResult(menuThreeVo);
    }

    //更新菜单
    @Override
    public ResponseResult updateMenu(MenuDto menuDto) {
        //判断MenuDto对象值是否为空
        if (!StringUtils.hasText(menuDto.getMenuName()) ||
                !StringUtils.hasText(menuDto.getMenuType()) ||
                !StringUtils.hasText(String.valueOf(menuDto.getStatus())) ||
                !StringUtils.hasText(menuDto.getPath()) ||
                !StringUtils.hasText(String.valueOf(menuDto.getOrderNum())) ||
                !StringUtils.hasText(menuDto.getIcon())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CONTENT_MENU_IS_BLANK);
        }
        // 将MenuDto对象转化为Menu对象
        Menu menu = BeanCopyUtils.copyBean(menuDto, Menu.class);
        updateById(menu);
        return ResponseResult.okResult();
    }

    //删除菜单
    @Override
    public ResponseResult deleteMenu(Long id) {
        //检查是否有子菜单
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getId, id);
        Menu menu = getOne(queryWrapper);
        if (Objects.isNull(menu.getParentId())){
            return ResponseResult.errorResult(AppHttpCodeEnum.DELETE_MENU_REFUSE);
        }
        //删除
        remove(queryWrapper);
        return ResponseResult.okResult();
    }
    /**
     * 查询菜单树
     *
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult getMenuTree() {
        // 复用之前的selectMenuList方法。方法需要参数，参数可以用来进行条件查询，而这个方法不需要条件，所以直接new Menu()传入
        List<Menu> menus = menuService.selectMenuList(new Menu());
        List<MenuTreeVo> menuTreeVos =  SystemConverter.buildMenuSelectTree(menus);
        return ResponseResult.okResult(menuTreeVos);
    }
    //加载对应角色菜单列表树接口
    @Override
    public ResponseResult roleMenuTreeselect(Long id) {
        //查询出所有菜单
        List<Menu> menus = menuService.selectMenuList(new Menu());
        //根据id查询出检索关键字
        List<Long> checkedKeys = this.selectMenuListByRoleId(id);
        //建立菜单树
        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        //封装
        RoleMenuTreeSelectVo vo = new RoleMenuTreeSelectVo(checkedKeys,menuTreeVos);
        return ResponseResult.okResult(vo);
    }
    //选择菜单树列表
    @Override
    public List<Menu> selectMenuList(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        //menuName模糊查询
        queryWrapper.like(StringUtils.hasText(menu.getMenuName()),Menu::getMenuName,menu.getMenuName());
        queryWrapper.eq(StringUtils.hasText(menu.getStatus()),Menu::getStatus,menu.getStatus());
        //排序 parent_id和order_num
        queryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> menus = list(queryWrapper);;
        return menus;
    }
    /**
     * 选择菜单通过角色id列表
     * @param roleId 角色id
     * @return {@link List}<{@link Long}>
     */
    public List<Long> selectMenuListByRoleId(Long roleId) {
        return getBaseMapper().selectMenuListByRoleId(roleId);
    }


}

