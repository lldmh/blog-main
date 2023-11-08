package com.mh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mh.constants.SystemConstants;
import com.mh.domain.ResponseResult;
import com.mh.domain.dto.RoleDto;
import com.mh.domain.entity.Role;
import com.mh.domain.entity.RoleMenu;
import com.mh.domain.vo.AdminRoleVo;
import com.mh.domain.vo.RoleTwoVo;
import com.mh.domain.vo.RoleVo;
import com.mh.enums.AppHttpCodeEnum;
import com.mh.mapper.RoleMapper;
import com.mh.service.RoleMenuService;
import com.mh.service.RoleService;
import com.mh.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.swing.plaf.LabelUI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-07-25 10:23:00
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private RoleMapper roleMapper;

    //根据用户id查询角色信息
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是返回集合中只需要有admin
        if(id == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    //角色列表
    @Override
    public ResponseResult getRoleList(Integer pageNum, Integer pageSize, String roleName) {
        //创建查询条件
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(roleName), Role::getRoleName, roleName);
        queryWrapper.eq(Role::getStatus, SystemConstants.NORMAL);
        queryWrapper.orderByAsc(Role::getRoleSort);
        //分页
        Page<Role> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<Role> roles = page.getRecords();
        //将role转化为roleVo
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(roles, RoleVo.class);
        //将roleVo转化为AdminRoleVo
        AdminRoleVo adminRoleVo = new AdminRoleVo(roleVos, page.getTotal());
        //返回
        return ResponseResult.okResult(adminRoleVo);
    }

    //改变角色状态
    @Override
    public ResponseResult changeStatus(RoleDto roleDto) {
        LambdaUpdateWrapper<Role> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Role::getId, roleDto.getId())
                .set(Role::getStatus, roleDto.getStatus());
        roleService.update(null, updateWrapper);
        return ResponseResult.okResult();
    }

    //添加角色
    @Override
    public ResponseResult addRole(RoleDto roleDto) {
//        1.根据角色名判断当前角色是否存在
        if (!this.judgeRole(roleDto.getRoleName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.ROLE_INFO_EXIST);
        }

//        1.根据权限名判断当前角色是否存在
        if (!this.judgeRoleKey(roleDto.getRoleKey())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.ROLEKEY_INFO_EXIST);
        }

//        2.获取到当前角色的菜单权限列表
        List<Long> menuIds = roleDto.getMenuIds();

//        3.将RoleDto对象转换为Role对象
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        save(role);

//        4.根据角色名获取到当前角色
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getRoleName, role.getRoleName());
        Role getRole = getOne(queryWrapper);

//        5.遍历menuIds，添加到sys_role_menu表中
        menuIds.stream()
                .map(menuId -> roleMenuService.save
                        (new RoleMenu(getRole.getId(), menuId)));
        return ResponseResult.okResult();
    }




    /**
     * 判断角色是否存在
     *
     * @param roleName
     * @return
     */
    public boolean judgeRole(String roleName) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Role::getRoleName, roleName);
        Role role = roleService.getOne(queryWrapper);
        if (Objects.isNull(role)) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 判断权限是否存在
     *
     * @param roleKey
     * @return
     */
    public boolean judgeRoleKey(String roleKey) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getRoleKey, roleKey);
        Role role = roleService.getOne(queryWrapper);
        if (Objects.isNull(role)) {
            return true;
        } else {
            return false;
        }
    }

    //查询角色
    @Override
    public ResponseResult getRoleById(Long id) {
        //根据id查询出角色信息
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getId, id);

        Role role = getOne(queryWrapper);
        //将角色信息封装到RoleTwoVo对象中
        RoleTwoVo roleTwoVo = BeanCopyUtils.copyBean(role, RoleTwoVo.class);
        return ResponseResult.okResult(roleTwoVo);
    }

    //修改角色
    @Override
    public ResponseResult updateRoleInfo(RoleDto roleDto) {
        //        1.判断LinkDto对象值是否为空
        if (!StringUtils.hasText(roleDto.getRoleName()) ||
                !StringUtils.hasText(roleDto.getRoleKey()) ||
                !StringUtils.hasText(String.valueOf(roleDto.getStatus())) ||
                !StringUtils.hasText(roleDto.getRemark()) ||
                !StringUtils.hasText(String.valueOf(roleDto.getRoleSort()))) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CONTENT_IS_BLANK);
        }

//        2.获取到当前角色的菜单权限列表
        List<Long> menuIds = roleDto.getMenuIds();

//        3.将RoleDto对象转换为Role对象
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        updateById(role);

//        4.查询当前角色的menuIds列表
        List<Long> roleMenuIdsById = roleMenuService.getRoleMenuIdsById(role.getId());
//        5.根据roleId移除sys_role_menu表中的数据
        roleMenuService.removeById(role.getId());
//          2.2遍历修改后的用户的menuIds列表，添加到sys_role_menu表中
        for (Long menuId : menuIds) {
            roleMenuService.save(new RoleMenu(role.getId(), menuId));
        }
        return ResponseResult.okResult();
    }

    //删除角色
    @Override
    public ResponseResult deleteRoleById(Long id) {
//        1.首先移除当前角色所关联的sys_role_menu表数据
        roleMenuService.removeById(id);
//        2.根据id移除角色
        removeById(id);
        return ResponseResult.okResult();
    }

    //查询角色列表
    @Override
    public ResponseResult getListAllRole() {
        List<Role> roles = roleMapper.selectList(null);
        return ResponseResult.okResult(roles);
    }

    //查询所有角色的列表
    @Override
    public List<Role> getAllRoleList() {
        List<Role> roles = roleMapper.selectList(null);
        return roles;
    }
}

