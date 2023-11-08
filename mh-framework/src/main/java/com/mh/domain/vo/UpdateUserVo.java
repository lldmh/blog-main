package com.mh.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author dmh
 * @Date 2023/7/31 20:54
 * @Version 1.0
 * @introduce
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserVo {
    private List<Long> roleIds;
    private List<RoleVo> roles;
    private UpdateUserInfoVo user;
}
