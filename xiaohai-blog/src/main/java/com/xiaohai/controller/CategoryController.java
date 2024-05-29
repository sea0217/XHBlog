package com.xiaohai.controller;

import com.xiaohai.domain.ResponseResult;
import com.xiaohai.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@Api(tags = "分类模块")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/getCategoryList")
    @ApiOperation(value = "查询所有分类", notes = "展示查询到的所有分类")
    public ResponseResult getCategoryList() {
        return categoryService.getCategoryList();
    }
}
