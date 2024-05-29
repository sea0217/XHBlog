package com.xiaohai.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.dto.ArticleQueryDto;
import com.xiaohai.domain.dto.CategoryQueryDto;
import com.xiaohai.domain.entity.Category;
import com.xiaohai.domain.vo.CategoryVo;
import com.xiaohai.domain.vo.ExcelCategoryVo;
import com.xiaohai.domain.vo.PageVo;
import com.xiaohai.enums.AppHttpCodeEnum;
import com.xiaohai.service.CategoryService;
import com.xiaohai.utils.BeanCopyUtils;
import com.xiaohai.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/listAllCategory")
    public ResponseResult<List<CategoryVo>> listAllCategory() {
        return categoryService.listAllCategory();
    }

    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        try {
            // 设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类表.xlsx", response);
            // 获取分类表的数据
            List<Category> categoryList = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryList, ExcelCategoryVo.class);

            // 调用工具类进行文件输出
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class)
                    .autoCloseStream(Boolean.FALSE)
                    .sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            // 如果出现异常也要响应json
            response.reset();
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, CategoryQueryDto categoryQueryDto) {
        return categoryService.selectCategoryPage(pageNum, pageSize, categoryQueryDto);
    }

    @PostMapping
    public ResponseResult addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    @GetMapping("/{id}")
    public ResponseResult getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PutMapping
    public ResponseResult updateCategory(@RequestBody Category category) {
        return categoryService.updateCategory(category);
    }

    @DeleteMapping
    public ResponseResult deleteBatchCategory(@RequestBody List<Long> ids) {
        return categoryService.deleteBatchCategory(ids);
    }
}
