package com.mh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mh.domain.ResponseResult;
import com.mh.domain.dto.AdminUserDto;
import com.mh.domain.dto.UpdateUserInfoRoleIdDto;
import com.mh.domain.dto.UserInfoDto;
import com.mh.domain.entity.Role;
import com.mh.domain.entity.User;
import com.mh.domain.entity.UserRole;
import com.mh.domain.vo.*;
import com.mh.enums.AppHttpCodeEnum;
import com.mh.exception.SystemException;
import com.mh.mapper.UserMapper;
import com.mh.service.AdminUserService;
import com.mh.service.RoleService;
import com.mh.service.UserRoleService;
import com.mh.service.UserService;
import com.mh.utils.BeanCopyUtils;
import com.mh.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @Author dmh
 * @Date 2023/7/31 19:33
 * @Version 1.0
 * @introduce
 */
@Service
public class AdminUserInfoServiceImpl implements AdminUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;

    //springSecurity提供的，在登录的时候用到了加密
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminUserService adminUserService;

    @Override
    public ResponseResult getUserInfoList(Integer pageNum, Integer pageSize, UserInfoDto userInfoDto) {
        //        1.根据用户名(模糊查询)和状态进行查询
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(userInfoDto.getStatus()), User::getStatus, userInfoDto.getStatus());
        queryWrapper.eq(StringUtils.hasText(userInfoDto.getPhonenumber()), User::getPhonenumber, userInfoDto.getPhonenumber());
        queryWrapper.like(StringUtils.hasText(userInfoDto.getUserName()), User::getUserName, userInfoDto.getUserName());

//        2.分页查询
        Page<User> page = new Page(pageNum, pageSize);
        userMapper.selectPage(page, queryWrapper);
        List<User> users = page.getRecords();

//        3.将当前页中的User对象转换为UserInfoVo对象
        List<UserInfoTwoVo> userInfoTwoVos = BeanCopyUtils.copyBeanList(users, UserInfoTwoVo.class);
////        4.将LinkVo对象转换为LinkAdminVo对象
        AdminUserVo adminUserVo = new AdminUserVo(userInfoTwoVos, page.getTotal());
        return ResponseResult.okResult(adminUserVo);
    }

    //添加用户
    @Override
    public ResponseResult addUser(AdminUserDto adminUserDto) {
        //        1.获取到AdminUserDto对象当中的roleIds属性
        List<Long> roleIds = adminUserDto.getRoleIds();
//        2.将AdminUserDto对象转化为User对象
        User user = BeanCopyUtils.copyBean(adminUserDto, User.class);
//        3.判断信息是否为空
        if (!StringUtils.hasText(user.getUserName()) ||
                !StringUtils.hasText(user.getNickName()) ||
                !StringUtils.hasText(user.getPassword()) ||
                !StringUtils.hasText(user.getEmail()) ||
                !StringUtils.hasText(user.getPhonenumber()) ||
                !StringUtils.hasText(user.getStatus()) ||
                !StringUtils.hasText(user.getSex())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_IS_BLANK);
        }
//        4.判断信息是否存在
        if (!judgeUsername(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (!judgePhoneNumber(user.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        if (!judgeEmail(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
//        5.保存用户
        userService.save(user);
//        6.获取到用户id
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,user.getUserName());
        User getUser = userService.getOne(queryWrapper);

//        7.向sys_user_role表中添加数据
//        roleIds.stream()
//                .map(roleId -> userRoleService.save(new UserRole(getUser.getId(),roleId)));
        roleIds.stream()
                .forEach(roleId -> userRoleService.save(new UserRole(getUser.getId(),roleId)));
        return ResponseResult.okResult();
    }

    //删除用户
    @Override
    public ResponseResult deleteUser(Long id) {
//        1.从SecurityContextHolder当中获取到当前登录用户的id
        Long userId = SecurityUtils.getUserId();
        if (userId == id) {
//            1.1如果是当前登录的用户则不允许删除
            return ResponseResult.errorResult(AppHttpCodeEnum.DELETE_USER_REFUSE);
        }
//        2.删除用户所对应sys_user_role表中的角色信息
        userRoleService.removeById(id);
        userService.removeById(id);
        return ResponseResult.okResult();
    }

    //通过id获取用户信息
    @Override
    public ResponseResult getUserInfoById(Long id) {
//        1.通过id查询用户信息
        User userById = userService.getById(id);
//          1.1将User对象转换为UpdateUserInfoVo对象
        UpdateUserInfoVo user = BeanCopyUtils.copyBean(userById, UpdateUserInfoVo.class);
//        2.查询用户所具有的角色id
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, id);
        List<Long> userRoleId = userRoleService.getUserRoleById(id);
//        3.查询所有角色的列表
        List<Role> allRoleList = roleService.getAllRoleList();
//            3.1将List<Role>对象转换为List<RoleVo>对象
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(allRoleList, RoleVo.class);

        UpdateUserVo updateUserVo = new UpdateUserVo(userRoleId, roleVos, user);

        return ResponseResult.okResult(updateUserVo);
    }

    //更新用户信息
    @Override
    public ResponseResult updateUserInfo(UpdateUserInfoRoleIdDto updateUserInfoRoleIdDto) {
//        1.获取到修改后的用户的roleIds列表
        List<Long> roleIds = updateUserInfoRoleIdDto.getRoleIds();
//        2.将UpdateUserInfoRoleIdVo对象转换为User对象
        User user = BeanCopyUtils.copyBean(updateUserInfoRoleIdDto, User.class);
//          2.1查询当前用户roleIds列表
        List<Long> userRoleById = userRoleService.getUserRoleById(user.getId());
//          2.2遍历修改后的用户的roleIds列表，如果有新增的就添加到sys_user_role表中
        for (Long roleId : roleIds) {
            if (!userRoleById.contains(roleId)) {
                userRoleService.save(new UserRole(user.getId(), roleId));
            }
        }

        //如果发现role有被删除的，及时删除user_role表中的数据
        //首先获得这个用户之前有哪些权限
//       查询用户所具有的角色id
        List<Long> userRoleIds = userRoleService.getUserRoleById(updateUserInfoRoleIdDto.getId());
        for (Long roleId : userRoleIds) {     //如果现有的权限里面没有以前就有的权限，则将以前有的那个权限删除
            if (!roleIds.contains(roleId)) {
                LambdaQueryWrapper<UserRole> queryWrapper1 = new LambdaQueryWrapper<>();
                queryWrapper1.eq(UserRole::getRoleId, roleId);
                userRoleService.remove(queryWrapper1);
            }
        }



//        2.根据用户id修改用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, user.getId());
        userService.update(user, queryWrapper);
        return ResponseResult.okResult();
    }


    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    public boolean judgeUsername(String username){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, username);
        User user = userService.getOne(queryWrapper);
        if (Objects.isNull(user)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断邮箱是否存在
     * @param email
     * @return
     */
    public boolean judgeEmail(String email){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User user = userService.getOne(queryWrapper);
        if (Objects.isNull(user)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断手机号是否存在
     * @param phonenumber
     * @return
     */
    public boolean judgePhoneNumber(String phonenumber){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhonenumber, phonenumber);
        User user = userService.getOne(queryWrapper);
        if (Objects.isNull(user)){
            return true;
        }else{
            return false;
        }
    }
}
