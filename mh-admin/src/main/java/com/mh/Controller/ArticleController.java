package com.mh.Controller;

import com.mh.domain.ResponseResult;
import com.mh.domain.dto.AddArticleDto;
import com.mh.domain.dto.AdminArticleDto;
import com.mh.domain.dto.ArticleDto;
import com.mh.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author dmh
 * @Date 2023/7/26 19:57
 * @Version 1.0
 * @introduce   新增博文接口
 */
@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    //新增博文
    @PostMapping("")
    public ResponseResult add(@RequestBody AddArticleDto article){
        return articleService.add(article);
    }

    // 查询文章列表
    @GetMapping("/list")
    public ResponseResult getArticleList(Integer pageNum, Integer pageSize, ArticleDto articleDto){
        return articleService.getArticleList(pageNum, pageSize, articleDto);
    }

    //根据id获取文章
    @GetMapping("/{id}")
    public ResponseResult getArticleById(@PathVariable("id") Long id){
        return articleService.getArticleById(id);
    }

    //更改文章
    @PutMapping("")
    public ResponseResult updateArticle(@RequestBody AdminArticleDto adminArticleDto){
        return articleService.updateArticle(adminArticleDto);
    }

    //删除文章
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable("id") Long id){
        return articleService.deleteArticle(id);
    }
}
