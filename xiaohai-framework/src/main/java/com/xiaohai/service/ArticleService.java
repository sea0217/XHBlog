package com.xiaohai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.dto.AddArticleDto;
import com.xiaohai.domain.dto.ArticleQueryDto;
import com.xiaohai.domain.entity.Article;
import com.xiaohai.domain.vo.PageVo;

/**
 * 文章表(Article)表服务接口
 *
 * @author makejava
 * @since 2024-05-06 19:11:18
 */
public interface ArticleService extends IService<Article> {
    // 查询热门文章
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult addArticle(AddArticleDto articleDto);

    ResponseResult<PageVo> selectArticleList(Integer pageNum, Integer pageSize, ArticleQueryDto articleQueryDto);

    ResponseResult<AddArticleDto> getArticle(Long id);

    ResponseResult updateArticle(AddArticleDto articleDto);

    //ResponseResult deleteArticle(Long id);

    ResponseResult deleteBatchArticle(Long[] ids);
}

