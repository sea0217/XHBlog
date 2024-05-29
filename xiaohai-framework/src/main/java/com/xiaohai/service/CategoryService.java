package com.xiaohai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.dto.CategoryQueryDto;
import com.xiaohai.domain.entity.Category;
import com.xiaohai.domain.vo.CategoryVo;
import com.xiaohai.domain.vo.PageVo;

import java.util.List;

/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2024-05-07 09:50:40
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult<PageVo> selectCategoryPage(Integer pageNum, Integer pageSize, CategoryQueryDto categoryQueryDto);

    ResponseResult addCategory(Category category);

    ResponseResult updateCategory(Category category);

    ResponseResult getCategoryById(Long id);

    ResponseResult deleteBatchCategory(List<Long> ids);

    ResponseResult<List<CategoryVo>> listAllCategory();
}

