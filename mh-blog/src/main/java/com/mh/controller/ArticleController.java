package com.mh.controller;

import com.mh.domain.ResponseResult;
import com.mh.domain.entity.Article;
import com.mh.service.ArticleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author dmh
 * @Date 2023/7/15 21:26
 * @Version 1.0
 * @introduce  文章控制类
 */

@RestController
@RequestMapping("/article")
@Api(tags = "文章",description = "文章相关接口")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/list")
    public List<Article> test(){
        return articleService.list();
    }

    //查询热门文章
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){
        //查询热门文章  封装成ResponseResult返回
        //hotArticleList()的方法逻辑要在service中实现，不要写在controller中
        ResponseResult result = articleService.hotArticleList();
        //返回
        return result;
    }

    //查询文章列表
    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Integer categoryId){
        return articleService.articleList(pageNum, pageSize, categoryId);
    }

    //查询文章的详细内容
    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }

    //更新阅读数
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}
