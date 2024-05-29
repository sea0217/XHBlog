package com.xiaohai.controller;

import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.dto.TagDto;
import com.xiaohai.domain.dto.TagListDto;
import com.xiaohai.domain.vo.PageVo;
import com.xiaohai.domain.vo.TagVo;
import com.xiaohai.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody TagListDto tagListDto) {
        return tagService.addTag(tagListDto);
    }

    //@DeleteMapping("/{id}")
    //public ResponseResult deleteTag(@PathVariable Long id) {
    //    return tagService.deleteTag(id);
    //}

    @DeleteMapping
    public ResponseResult deleteBatchTag(@RequestBody List<Long> ids) {
        return tagService.deleteBatchTag(ids);
    }

    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable Long id) {
        return tagService.getTag(id);
    }

    @PutMapping
    public ResponseResult updateTag(@RequestBody TagDto tagDto) {
        return tagService.updateTag(tagDto);
    }

    @GetMapping("/listAllTag")
    public ResponseResult<List<TagVo>> listAllTag() {
        return tagService.listAllTag();
    }
}
