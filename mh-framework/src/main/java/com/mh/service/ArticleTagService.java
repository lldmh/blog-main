package com.mh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mh.domain.ResponseResult;
import com.mh.domain.dto.AddArticleDto;
import com.mh.domain.entity.ArticleTag;

import java.util.List;


/**
 * 文章标签关联表(ArticleTag)表服务接口
 *
 * @author makejava
 * @since 2023-07-26 20:03:12
 */
public interface ArticleTagService extends IService<ArticleTag> {
    List<Long> getTagList(Long id);
}

