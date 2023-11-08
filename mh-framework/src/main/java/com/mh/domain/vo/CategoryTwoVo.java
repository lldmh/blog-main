package com.mh.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author dmh
 * @Date 2023/7/26 20:44
 * @Version 1.0
 * @introduce
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryTwoVo {
    private Long id;
    private String name;
    private String status;
    private String description;
}
