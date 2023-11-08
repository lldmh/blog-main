package com.mh.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author dmh
 * @Date 2023/7/26 20:46
 * @Version 1.0
 * @introduce
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCategoryVo {
    private List<CategoryTwoVo> rows;
    private Long total;
}