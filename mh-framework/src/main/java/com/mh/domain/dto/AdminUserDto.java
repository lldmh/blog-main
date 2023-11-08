package com.mh.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author dmh
 * @Date 2023/7/31 19:58
 * @Version 1.0
 * @introduce
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserDto {
    private String userName;

    private String nickName;

    private String password;

    private String phonenumber;

    private String email;

    private String sex;

    private String status;

    private List<Long> roleIds;
}
