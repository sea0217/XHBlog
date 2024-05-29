package com.xiaohai.controller;

import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.dto.AddArticleDto;
import com.xiaohai.domain.dto.ArticleQueryDto;
import com.xiaohai.domain.vo.PageVo;
import com.xiaohai.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @PostMapping
    public ResponseResult addArticle(@RequestBody AddArticleDto articleDto){
        return articleService.addArticle(articleDto);
    }

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, ArticleQueryDto articleQueryDto){
        return articleService.selectArticleList(pageNum,pageSize,articleQueryDto);
    }

    @GetMapping("/{id}")
    public ResponseResult<AddArticleDto> getArticle(@PathVariable Long id){
        return articleService.getArticle(id);
    }

    @PutMapping
    public ResponseResult updateArticle(@RequestBody AddArticleDto articleDto){
        return articleService.updateArticle(articleDto);
    }

    //@DeleteMapping("/{id}")
    //public ResponseResult deleteArticle(@PathVariable Long id){
    //    return articleService.deleteArticle(id);
    //}

    @DeleteMapping
    public ResponseResult deleteBatchArticle(@RequestBody Long[] ids){
        return articleService.deleteBatchArticle(ids);
    }
}
