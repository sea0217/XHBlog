package com.xiaohai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohai.constants.SystemConstants;
import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.dto.CategoryQueryDto;
import com.xiaohai.domain.entity.Article;
import com.xiaohai.domain.entity.Category;
import com.xiaohai.domain.vo.CategoryVo;
import com.xiaohai.domain.vo.PageVo;
import com.xiaohai.enums.AppHttpCodeEnum;
import com.xiaohai.exception.SystemException;
import com.xiaohai.mapper.CategoryMapper;
import com.xiaohai.service.ArticleService;
import com.xiaohai.service.CategoryService;
import com.xiaohai.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2024-05-07 09:50:40
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;
    @Override
    public ResponseResult getCategoryList() {
        // 查询文章表中状态为正常的文章
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);
        // 获取文章的分类id，并且去重
        Set<Long> categoryIds = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());
        // 查询分类表
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());

        // 封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult<PageVo> selectCategoryPage(Integer pageNum, Integer pageSize, CategoryQueryDto categoryQueryDto) {

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(categoryQueryDto.getName()), Category::getName, categoryQueryDto.getName());
        queryWrapper.eq(StringUtils.hasText(categoryQueryDto.getStatus()), Category::getStatus, categoryQueryDto.getStatus());

        Page<Category> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        return ResponseResult.okResult(new PageVo(page.getRecords(), page.getTotal()));
    }

    @Override
    public ResponseResult addCategory(Category category) {
        // 判断分类名是否重复
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getName, category.getName());
        long count = count(queryWrapper);

        if (count > 0) {
            throw new SystemException(AppHttpCodeEnum.CATEGORY_EXIST);
        }

        save(category);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateCategory(Category category) {
        // 如果分类名改变，就判断分类名是否重复
        if (!category.getName().equals(category.getOldName())) {
            LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Category::getName, category.getName());
            long count = count(queryWrapper);

            if (count > 0) {
                throw new SystemException(AppHttpCodeEnum.CATEGORY_EXIST);
            }
        }

        updateById(category);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCategoryById(Long id) {
        // 查询分类表
        Category category = getById(id);
        return ResponseResult.okResult(category);
    }

    @Override
    public ResponseResult deleteBatchCategory(List<Long> ids) {
        // 逻辑删除分类表
        UpdateWrapper<Category> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("del_flag", SystemConstants.IS_DELETED_TRUE);
        updateWrapper.in("id", ids);

        update(updateWrapper);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<List<CategoryVo>> listAllCategory() {
        // 查询分类表
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus, SystemConstants.STATUS_NORMAL);
        List<Category> categories = list(queryWrapper);

        // 封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }
}

