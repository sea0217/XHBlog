package com.xiaohai.job;

import com.xiaohai.constants.CacheConstants;
import com.xiaohai.domain.entity.Article;
import com.xiaohai.service.ArticleService;
import com.xiaohai.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;
    @Scheduled(cron = "0/30 * * * * ?")
    public void updateViewCount(){
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(CacheConstants.ARTICLE_VIEWCOUNT);
        List<Article> articles = viewCountMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        //更新数据库
        articleService.updateBatchById(articles);
    }
}
