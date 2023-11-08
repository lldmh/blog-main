package com.mh.job;

import com.mh.constants.SystemConstants;
import com.mh.domain.entity.Article;
import com.mh.service.ArticleService;
import com.mh.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author dmh
 * @Date 2023/7/23 16:06
 * @Version 1.0
 * @introduce    定时任务每隔10分钟把Redis中的浏览量更新到数据库中
 */
@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateViewCount(){
        System.out.println("更新一次updateViewCount");
        //获取redis中的浏览量，viewCountMap是一个双链集合
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.ARTICLE_VIEW_COUNT);

        List<Article> articles = viewCountMap.entrySet()  //entrySet变成单链集合
            .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());

        //更新到数据库中
        articleService.updateBatchById(articles);
    }
}
