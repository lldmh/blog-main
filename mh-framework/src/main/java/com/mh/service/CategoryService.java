package com.mh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mh.domain.ResponseResult;
import com.mh.domain.dto.CategoryDto;
import com.mh.domain.entity.Category;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-07-16 19:12:09
 */
public interface CategoryService extends IService<Category> {
    //查询文章分类
    ResponseResult getCategoryList();
    //查询所有类别
    ResponseResult listAllCategory();
    //查询文章类别列表
    ResponseResult getArticleCategoryList(Integer pageNum, Integer pageSize, CategoryDto categoryDto);
    //添加文章类别
    ResponseResult addCategory(CategoryDto categoryDto);
    //根据id查询分类,回显
    ResponseResult getCategoryById(Long id);
    //更新分类
    ResponseResult updateCategory(CategoryDto categoryDto);
    //删除分类
    ResponseResult deleteCategoryById(Long id);
}

