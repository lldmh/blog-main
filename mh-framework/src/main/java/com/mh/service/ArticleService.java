package com.mh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mh.domain.ResponseResult;
import com.mh.domain.dto.AddArticleDto;
import com.mh.domain.dto.AdminArticleDto;
import com.mh.domain.dto.ArticleDto;
import com.mh.domain.entity.Article;

/**
 * @Author dmh
 * @Date 2023/7/15 21:21
 * @Version 1.0
 */

public interface ArticleService extends IService<Article> {
    //查询热门文章
    ResponseResult hotArticleList();
    //查询文章列表
    ResponseResult articleList(Integer pageNum, Integer pageSize, Integer categoryId);
    //查询文章的详细内容
    ResponseResult getArticleDetail(Long id);
    //更新阅读数
    ResponseResult updateViewCount(Long id);
    //新增博文
    ResponseResult add(AddArticleDto article);
    //查询文章列表
    ResponseResult getArticleList(Integer pageNum, Integer pageSize, ArticleDto articleDto);
    //根据id获取文章
    ResponseResult getArticleById(Long id);
    //更改文章
    ResponseResult updateArticle(AdminArticleDto adminArticleDto);
    //删除文章
    ResponseResult deleteArticle(Long id);
}
