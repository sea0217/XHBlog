package com.xiaohai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.dto.TagDto;
import com.xiaohai.domain.dto.TagListDto;
import com.xiaohai.domain.entity.Tag;
import com.xiaohai.domain.vo.PageVo;
import com.xiaohai.domain.vo.TagVo;

import java.util.List;

/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2024-05-19 16:20:39
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(TagListDto tagListDto);

    //ResponseResult deleteTag(Long id);

    ResponseResult getTag(Long id);

    ResponseResult updateTag(TagDto tagDto);

    ResponseResult<List<TagVo>> listAllTag();

    ResponseResult deleteBatchTag(List<Long> ids);
}

