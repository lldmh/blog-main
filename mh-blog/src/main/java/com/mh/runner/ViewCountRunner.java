package com.mh.runner;

import com.mh.constants.SystemConstants;
import com.mh.domain.entity.Article;
import com.mh.mapper.ArticleMapper;
import com.mh.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author dmh
 * @Date 2023/7/23 15:24
 * @Version 1.0
 * @introduce    文章浏览量的Runner，实现项目启动时预处理:在应用启动时把博客的浏览量存储到redis中
 */
@Component
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        //1、查询博客信息  id  viewCount(从数据库中查询出来)
        List<Article> articles = articleMapper.selectList(null);
        //用stream流的方式将文章的id和viewcount的值放在map中，，，id要变成String类型
        Map<String, Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(new Function<Article, String>() {    //Article是传入的参数，Long是返回值的类型
                    @Override
                    public String apply(Article article) {
                        return article.getId().toString();
                    }
                }, new Function<Article, Integer>() {
                    @Override
                    public Integer apply(Article article) {
                        return article.getViewCount().intValue();
                    }
                }));
        //2、存储到redies中
        redisCache.setCacheMap(SystemConstants.ARTICLE_VIEW_COUNT, viewCountMap);
        //已将viewCount放进redis中了
        System.out.println("已将viewCount放进redis中了");
    }
}
