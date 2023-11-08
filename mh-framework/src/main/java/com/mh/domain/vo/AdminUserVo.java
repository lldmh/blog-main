package com.mh.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author dmh
 * @Date 2023/7/31 19:35
 * @Version 1.0
 * @introduce
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserVo {
    private List<UserInfoTwoVo> rows;
    private Long total;
}
