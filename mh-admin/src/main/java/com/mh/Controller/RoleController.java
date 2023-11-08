package com.mh.Controller;

import com.mh.domain.ResponseResult;
import com.mh.domain.dto.RoleDto;
import com.mh.domain.vo.RoleVo;
import com.mh.service.MenuService;
import com.mh.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author dmh
 * @Date 2023/7/28 21:23
 * @Version 1.0
 * @introduce
 */
@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    //角色列表
    @GetMapping("/list")
    public ResponseResult getRoleList(Integer pageNum, Integer pageSize, String roleName){
        return roleService.getRoleList(pageNum, pageSize, roleName);
    }

    //改变角色状态
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody RoleDto roleDto){
        return roleService.changeStatus(roleDto);
    }

    //添加角色
    @PostMapping
    public ResponseResult addRole(@RequestBody RoleDto roleDto){
        return roleService.addRole(roleDto);
    }

    //根据id查询角色
    @GetMapping("/{id}")
    public ResponseResult getRoleById(@PathVariable("id") Long id){
        return roleService.getRoleById(id);
    }

    //修改角色
    @PutMapping("")
    public ResponseResult updateRoleById(@RequestBody RoleDto roleDto){
        return roleService.updateRoleInfo(roleDto);
    }

    //删除角色
    @DeleteMapping("/{id}")
    public ResponseResult deleteRoleById(@PathVariable("id") Long id){
        return roleService.deleteRoleById(id);
    }

    //查询角色列表
    @GetMapping("/listAllRole")
    public ResponseResult getListAllRole(){
        return roleService.getListAllRole();
    }
}
