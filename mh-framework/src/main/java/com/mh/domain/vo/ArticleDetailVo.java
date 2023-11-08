package com.mh.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author dmh
 * @Date 2023/7/17 19:14
 * @Version 1.0
 * @introduce  文章细节Vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailVo {
    private Long id;
    //标题
    private String title;
    //文章摘要
    private String summary;
    //所属分类id
    private Long categoryId;
    //所属分类名(这个没有，就不会拷贝过来)
    private String categoryName;
    //缩略图
    private String thumbnail;

    //文章内容
    private String content;

    //访问量
    private Long viewCount;

    private Date createTime;
}
