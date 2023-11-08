package com.mh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mh.constants.SystemConstants;
import com.mh.domain.ResponseResult;
import com.mh.domain.dto.CategoryDto;
import com.mh.domain.entity.Article;
import com.mh.domain.entity.Category;
import com.mh.domain.vo.AdminCategoryVo;
import com.mh.domain.vo.CategoryTwoVo;
import com.mh.domain.vo.CategoryVo;
import com.mh.domain.vo.PageVo;
import com.mh.enums.AppHttpCodeEnum;
import com.mh.mapper.CategoryMapper;
import com.mh.service.ArticleService;
import com.mh.service.CategoryService;
import com.mh.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-07-16 19:12:10
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    //这个是文章的service
    @Autowired
    private ArticleService articleService;

    //查询文章分类列表
    @Override
    public ResponseResult getCategoryList() {
        //查询文章表   状态为已发布0的文章
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL); //等值的判断eq
        List<Article> articleList = articleService.list(articleWrapper);   //因为不需要做分页，所以直接用的list
        //获取文章的分类id ，并去重
        Set<Long> categoryIds = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());
        //查询分类表
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream().
                filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //封装VO
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    //查询所有类别
    @Override
    public ResponseResult<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus, SystemConstants.NORMAL);
        List<Category> categories = list(queryWrapper);
        //拷贝bean
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    /**
     * 查询文章类别列表
     *
     * @param pageNum     页面num
     * @param pageSize    页面大小
     * @param categoryDto 类别dto
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult getArticleCategoryList(Integer pageNum, Integer pageSize, CategoryDto categoryDto) {
        //根据文章分类名(模糊查询)和状态进行查询
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(categoryDto.getName()),  Category::getName, categoryDto.getName());  //Category对象的status属性与categoryDto对象的status属性相等
        queryWrapper.eq(StringUtils.hasText(categoryDto.getStatus()), Category::getStatus, categoryDto.getStatus());
        //分页查询
        Page<Category> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        //将当前页中的Category对象转换为CategoryTwoVo对象
        List<Category> categories = page.getRecords();
        List<CategoryTwoVo> categoryTwoVos = BeanCopyUtils.copyBeanList(categories, CategoryTwoVo.class);
        //将CategoryTwoVo对象转换为AdminCategoryVo对象
        AdminCategoryVo adminCategoryVo = new AdminCategoryVo(categoryTwoVos, page.getTotal());

        return ResponseResult.okResult(adminCategoryVo);
    }

    //添加文章类别
    @Override
    public ResponseResult addCategory(CategoryDto categoryDto) {
//        1.首先根据分类名称查询要添加的类别是否存在
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Category::getName, categoryDto.getName());
        Category category = getOne(queryWrapper);
        if (!Objects.isNull(category)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CATEGORY_IS_EXIST);
        }
//        2.添加分类
//          2.1将CategoryDto对象转为Category对象
        Category addCategory = BeanCopyUtils.copyBean(categoryDto, Category.class);
        save(addCategory);
        return ResponseResult.okResult();
    }

    //根据id查询分类,回显
    @Override
    public ResponseResult getCategoryById(Long id) {
//        1.根据id查询友链
        Category category = getById(id);
//        2.将Link对象封装为LinkVo对象
        CategoryTwoVo categoryVo = BeanCopyUtils.copyBean(category, CategoryTwoVo.class);
        return ResponseResult.okResult(categoryVo);
    }

    //更新分类
    @Override
    public ResponseResult updateCategory(CategoryDto categoryDto) {
//        1.判断categoryDto对象值是否为空
        if (!StringUtils.hasText(categoryDto.getName()) &&
                !StringUtils.hasText(categoryDto.getDescription()) &&
                !StringUtils.hasText(String.valueOf(categoryDto.getStatus()))) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CONTENT_IS_BLANK);
        }
//        2.将categoryDto对象转换为category对象
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
        updateById(category);
        return ResponseResult.okResult();
    }

    //删除分类
    @Override
    public ResponseResult deleteCategoryById(Long id) {
        boolean result = removeById(id);
        if (result == false) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DELETE_CATEGORY_FAIL);
        }
        return ResponseResult.okResult();
    }
}

