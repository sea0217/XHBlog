package com.xiaohai.controller;

import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.dto.LinkListDto;
import com.xiaohai.domain.dto.TagDto;
import com.xiaohai.domain.dto.TagListDto;
import com.xiaohai.domain.entity.Link;
import com.xiaohai.domain.vo.PageVo;
import com.xiaohai.domain.vo.TagVo;
import com.xiaohai.service.LinkService;
import com.xiaohai.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, LinkListDto linkListDto)  {
        return linkService.listPage(pageNum,pageSize,linkListDto);
    }

    @PostMapping
    public  ResponseResult addLink(@RequestBody Link link) {
        return linkService.addLink(link);
    }

    @GetMapping("/{id}")
    public  ResponseResult<Link> getLink(@PathVariable Long id) {
        return linkService.getLink(id);
    }

    @PutMapping
    public  ResponseResult updateLink(@RequestBody Link link) {
        return linkService.updateLink(link);
    }

    @DeleteMapping
    public  ResponseResult deleteBatchLink(@RequestBody List<Long> ids) {
        return linkService.deleteBatchLink(ids);
    }
}
