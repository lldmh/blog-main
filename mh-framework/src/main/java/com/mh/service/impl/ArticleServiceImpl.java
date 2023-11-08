package com.mh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mh.constants.SystemConstants;
import com.mh.domain.ResponseResult;
import com.mh.domain.dto.AddArticleDto;
import com.mh.domain.dto.AdminArticleDto;
import com.mh.domain.dto.ArticleDto;
import com.mh.domain.entity.Article;
import com.mh.domain.entity.ArticleTag;
import com.mh.domain.entity.Category;
import com.mh.domain.vo.*;
import com.mh.enums.AppHttpCodeEnum;
import com.mh.mapper.ArticleMapper;
import com.mh.service.ArticleService;
import com.mh.service.ArticleTagService;
import com.mh.service.CategoryService;
import com.mh.utils.BeanCopyUtils;
import com.mh.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * @Author dmh
 * @Date 2023/7/15 21:22
 * @Version 1.0
 * @introduce  文章服务层接口实现类
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

    //查询热门文章
    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章  封装成ResponseResult返回
        //创建wrapper(装饰者模式)
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章(可以写字段名也可以用   方法引用  的方式（推荐）)
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量排序(可以写字段名也可以用   方法引用  的方式（推荐）)
        queryWrapper.orderByDesc(Article::getViewCount);
        //做多查询十条(分页)
        Page<Article> page = new Page<>(1, 10);
        page(page, queryWrapper);

        //放入列表中
        List<Article> articles = page.getRecords();

        //实现bean拷贝，原始的bean转化为vo的优化(字段名字和类型相同才能拷贝过去)
//        List<HotArticleVo> articleVos = new ArrayList<>();
//        for (Article article : articles){
//            HotArticleVo vo = new HotArticleVo();
//            BeanUtils.copyProperties(article, vo);
//            articleVos.add(vo);
//        }
        List<HotArticleVo> vs = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);


        //直接用ResponseResult的静态方法进行封装
        return ResponseResult.okResult(vs);
    }

    //查询文章列表
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Integer categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        // 如果 有categoryId 就要 查询时要和传入的相同，首页不需要，到了具体文章类型展示页面才需要(查询分类文章和全部文章共用)
        articleWrapper.eq(Objects.nonNull(categoryId)&&categoryId > 0,
                Article::getCategoryId, categoryId);
        //状态为正式发布的
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //要对isTop进行降序排列
        articleWrapper.orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, articleWrapper);
        List<Article> articles = page.getRecords();

        //查询categoryName
        //articleId去查询articleName进行设置
//        articles.stream()
//                .map(new Function<Article, Article>() {
//                    @Override
//                    public Article apply(Article article) {
//                        //获取分类id，查询分类信息，获取分类名称
////                        Category category =
////                        String name = category.getName();
//                        //把分类名称设置为article
////                        article.setCategoryName(name);
//                        //这里返回的是空，并不是article，需要用Lombok实现直接返回article
//                        //在article中添加@Accessors(chain = true)   //这样set方法返回的值就是对象本身了
//                        return article.setCategoryName(categoryService.getById(article.getCategoryId()).getName());
////                        return article;
//                    }
//                });
        articles.stream()
                .map(article ->
                        article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());

//        for (Article article : articles) {
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }
        //封装，拷贝bean,封装ArticleListVo
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);

        //封装
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    //查询文章的详细内容
    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章（直接使用mybatisplus中的方法，应为直接与ariticleMapper接口联系，所以可以直接使用方法）
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        //转换成VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名称
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if (category != null){  //当前类别存在的话
            articleDetailVo.setCategoryName(category.getName());
        }

        //封装相应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    //更新阅读数
    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应的id的浏览量
        redisCache.incrementCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT, id.toString(), 1);
        return ResponseResult.okResult();
    }

    //新增博文
    @Override
    @Transactional
    public ResponseResult add(AddArticleDto articleDto) {
        //添加 博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);


        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    //查询文章列表
    @Override
    public ResponseResult getArticleList(Integer pageNum, Integer pageSize, ArticleDto articleDto) {
        //根据文章标题和摘要(模糊查询),状态为非删除的
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(articleDto.getTitle()), Article::getTitle, articleDto.getTitle());
        queryWrapper.like(StringUtils.hasText(articleDto.getSummary()), Article::getSummary, articleDto.getSummary());
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<Article> articles = page.getRecords();
        //将当前页中的Article对象转换为ArticleVo对象
        List<ArticleVo> articleVos = BeanCopyUtils.copyBeanList(articles, ArticleVo.class);
        //将ArticleVo对象转化成AdminArticleVo对象
        AdminArticleVo adminArticleVo = new AdminArticleVo(articleVos, page.getTotal());
        return ResponseResult.okResult(adminArticleVo);
    }

    //根据id获取文章
    @Override
    public ResponseResult getArticleById(Long id) {
        //根据id查询对应的文章内容
        Article article = getById(id);
        //将查询到的Article装换成AddArticleVo
        UpdateArticleVo updateArticleVo = BeanCopyUtils.copyBean(article, UpdateArticleVo.class);
        //根据文章id获取到文章标签列表
        List<Long> tagList = articleTagService.getTagList(id);

        updateArticleVo.setTags(tagList);
        //返回
        return ResponseResult.okResult(updateArticleVo);
    }

    //更改文章
    @Override
    public ResponseResult updateArticle(AdminArticleDto adminArticleDto) {
        // 1.将AdminArticleDto对象转换为AddArticleVo对象
        UpdateArticleVo updateArticleVo = BeanCopyUtils.copyBean(adminArticleDto, UpdateArticleVo.class);
        //  2.将博客的标签信息存入标签表
        //      2.1根据当前博客id获取到已有的标签列表
        List<Long> tagList = articleTagService.getTagList(updateArticleVo.getId());
        //      2.2得到修改过后的标签列表
        List<Long> tags = updateArticleVo.getTags();
        //      2.3遍历修改过后的标签列表，判断当前博客是否已经有此标签，没有则一条数据添加到sg_article_tag表中
        for (Long tag:tags){
            if (!tagList.contains(tag)){
                articleTagService.save(new ArticleTag(updateArticleVo.getId(), tag));
            }
        }
        //更新
        Article article = BeanCopyUtils.copyBean(updateArticleVo, Article.class);
        updateById(article);
        return ResponseResult.okResult();
    }

    //删除文章
    @Override
    public ResponseResult deleteArticle(Long id) {
        boolean result = removeById(id);
        //如果没删除成功，抛出异常
        if(result == false){
            return ResponseResult.errorResult(AppHttpCodeEnum.DELETE_ARTICLE_FAIL);
        }
        return ResponseResult.okResult();
    }
}
