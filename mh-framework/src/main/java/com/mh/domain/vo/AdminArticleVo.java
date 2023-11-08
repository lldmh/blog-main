package com.mh.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author dmh
 * @Date 2023/7/27 20:35
 * @Version 1.0
 * @introduce
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminArticleVo {
    private List<ArticleVo> rows;
    private Long total;
}
