package com.xiaohai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohai.constants.CacheConstants;
import com.xiaohai.constants.SystemConstants;
import com.xiaohai.domain.dto.AddArticleDto;
import com.xiaohai.domain.dto.ArticleQueryDto;
import com.xiaohai.domain.entity.ArticleTag;
import com.xiaohai.domain.entity.Category;
import com.xiaohai.domain.vo.ArticleDetailVo;
import com.xiaohai.domain.vo.ArticleListVo;
import com.xiaohai.domain.vo.PageVo;
import com.xiaohai.mapper.ArticleMapper;
import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.entity.Article;
import com.xiaohai.domain.vo.HotArticleVo;
import com.xiaohai.service.ArticleService;
import com.xiaohai.service.ArticleTagService;
import com.xiaohai.service.CategoryService;
import com.xiaohai.utils.BeanCopyUtils;
import com.xiaohai.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author makejava
 * @since 2024-05-06 19:13:14
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章，封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只查询10条
        Page<Article> page = new Page<>(1, 10);
        page(page, queryWrapper);

        List<Article> articles = page.getRecords();
        // 从redis中获取浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(CacheConstants.ARTICLE_VIEWCOUNT);
        for (Article article : articles) {
            Integer viewCount = viewCountMap.get(article.getId().toString());
            article.setViewCount(viewCount.longValue());
        }

        //copy到VO对象中
        //List<HotArticleVo> articleVos = new ArrayList<>();
        //for (Article article : articles) {
        //    HotArticleVo hotArticleVo = new HotArticleVo();
        //    BeanUtils.copyProperties(article, hotArticleVo);
        //    articleVos.add(hotArticleVo);
        //}


        List<HotArticleVo> articleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(articleVos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        // 查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 如果 有categoryId 就要 查询时要和传入的相同
        queryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);
        // 状态是正式发布的
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // 对isTop进行降序
        queryWrapper.orderByDesc(Article::getIsTop);
        // 分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<Article> articles = page.getRecords();

        // 从redis中获取浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(CacheConstants.ARTICLE_VIEWCOUNT);
        for (Article article : articles) {
            Integer viewCount = viewCountMap.get(article.getId().toString());
            article.setViewCount(viewCount.longValue());
        }
        
        // 查询categoryName
        for (Article article : articles) {
            Long articleCategoryId = article.getCategoryId();
            Category category = categoryService.getById(articleCategoryId);

            if(category!=null){
                article.setCategoryName(category.getName());
            }
        }

        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        // 根据id查询文章
        Article article = getById(id);
        // 从redis中获取浏览量
        Integer viewCount = redisCache.getCacheMapValue(CacheConstants.ARTICLE_VIEWCOUNT, id.toString());
        article.setViewCount(viewCount.longValue());
        // 拷贝到VO对象中
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        // 根据分类id查询分类名称
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        String categoryName = category.getName();

        // 设置分类名称
        if (category != null) {
            articleDetailVo.setCategoryName(categoryName);
        }
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        // 更新redis中对应id的浏览量
        redisCache.incrementCacheMapValue(CacheConstants.ARTICLE_VIEWCOUNT, String.valueOf(id), 1);
        return ResponseResult.okResult();
    }

    @Override
    //添加事务注解
    @Transactional
    public ResponseResult addArticle(AddArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);

        List<ArticleTag> tagList = articleDto.getTags()
                .stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        articleTagService.saveBatch(tagList);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<PageVo> selectArticleList(Integer pageNum, Integer pageSize, ArticleQueryDto articleQueryDto) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(articleQueryDto.getTitle()), Article::getTitle, articleQueryDto.getTitle());
        queryWrapper.eq(StringUtils.hasText(articleQueryDto.getStatus()), Article::getStatus, articleQueryDto.getStatus());
        queryWrapper.eq(StringUtils.hasText(articleQueryDto.getSummary()), Article::getSummary, articleQueryDto.getSummary());

        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        //封装数据
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult<AddArticleDto> getArticle(Long id) {
        Article article = getById(id);
        AddArticleDto articleDto = BeanCopyUtils.copyBean(article, AddArticleDto.class);

        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, id);

        List<ArticleTag> articleTags = articleTagService.list(queryWrapper);

        List<Long> tags = articleTags.stream()
                .map(ArticleTag::getTagId)
                .collect(Collectors.toList());

        articleDto.setTags(tags);

        return ResponseResult.okResult(articleDto);
    }

    @Override
    @Transactional
    public ResponseResult updateArticle(AddArticleDto articleDto) {

        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        updateById(article);

        // 删除旧标签
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, article.getId());
        articleTagService.remove(queryWrapper);

        // 添加新标签
        List<ArticleTag> tagList = articleDto.getTags()
                                    .stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        articleTagService.saveBatch(tagList);

        return ResponseResult.okResult();
    }

    //@Override
    //public ResponseResult deleteArticle(Long id) {
    //    // 逻辑删除文章
    //    UpdateWrapper<Article> updateWrapper = new UpdateWrapper<>();
    //    updateWrapper.set("del_flag", 1);
    //    updateWrapper.eq("id", id);
    //    update(updateWrapper);
    //
    //    // 删除文章对应的标签
    //    LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
    //    queryWrapper.eq(ArticleTag::getArticleId, id);
    //    articleTagService.remove(queryWrapper);
    //
    //    return ResponseResult.okResult();
    //}

    @Override
    @Transactional
    public ResponseResult deleteBatchArticle(Long[] ids) {
        // 逻辑删除文章
        UpdateWrapper<Article> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("del_flag", SystemConstants.IS_DELETED_TRUE);
        updateWrapper.in("id", Arrays.asList(ids));
        update(updateWrapper);

        // 删除文章对应的标签
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ArticleTag::getArticleId, Arrays.asList(ids));
        articleTagService.remove(queryWrapper);

        return ResponseResult.okResult();
    }
}

