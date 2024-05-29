package com.xiaohai.controller;

import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.entity.Article;
import com.xiaohai.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章表(Article)表控制层
 *
 * @author makejava
 * @since 2024-05-06 19:16:14
 */
@RestController
@RequestMapping("/article")
@Api(tags = "文章模块", description = "文章模块相关接口")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    //@GetMapping("/getAll")
    //public List<Article> getAll(){
    //    return articleService.list();
    //}

    @GetMapping("/hotArticleList")
    @ApiOperation(value = "热门文章", notes = "展示热门文章")
    public ResponseResult hotArticleList() {
        // 查询热门文章，封装成ResponseResult返回
        return articleService.hotArticleList();
    }

    @GetMapping("/articleList")
    @ApiOperation(value = "文章列表", notes = "分页展示文章列表")
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        return articleService.articleList(pageNum, pageSize, categoryId);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "文章详情", notes = "展示文章详情")
    public ResponseResult getArticleDetail(@PathVariable Long id) {
        return articleService.getArticleDetail(id);
    }

    @PutMapping("/updateViewCount/{id}")
    @ApiOperation(value = "更新文章浏览量", notes = "更新文章浏览量")
    public ResponseResult updateViewCount(@PathVariable Long id) {
        return articleService.updateViewCount(id);
    }
}

