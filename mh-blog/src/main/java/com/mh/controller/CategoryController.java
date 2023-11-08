package com.mh.controller;

import com.mh.domain.ResponseResult;
import com.mh.service.CategoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author dmh
 * @Date 2023/7/16 19:44
 * @Version 1.0
 * @introduce 文章分类控制类
 */
@RestController
@RequestMapping("/category")
@Api(tags = "文章类别",description = "文章类别相关接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //查询文章分类
    @GetMapping("/getCategoryList")
    public ResponseResult getCategoryList(){
        return categoryService.getCategoryList();
    }
}
