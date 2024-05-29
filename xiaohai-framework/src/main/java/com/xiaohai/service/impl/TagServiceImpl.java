package com.xiaohai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohai.constants.SystemConstants;
import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.dto.TagDto;
import com.xiaohai.domain.dto.TagListDto;
import com.xiaohai.domain.entity.Tag;
import com.xiaohai.domain.vo.PageVo;
import com.xiaohai.domain.vo.TagVo;
import com.xiaohai.enums.AppHttpCodeEnum;
import com.xiaohai.exception.SystemException;
import com.xiaohai.mapper.TagMapper;
import com.xiaohai.service.TagService;
import com.xiaohai.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2024-05-19 16:20:39
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        // 创建page对象
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()), Tag::getName, tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()), Tag::getRemark, tagListDto.getRemark());

        Page<Tag> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        // 封装VO返回对象
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(TagListDto tagListDto) {

        // 判断标签是否重复
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getName, tagListDto.getName());
        if (getOne(queryWrapper) != null) {
            throw new SystemException(AppHttpCodeEnum.NAME_EXIST_ERROR);
        }

        // 添加标签
        Tag tag = BeanCopyUtils.copyBean(tagListDto, Tag.class);
        save(tag);

        return ResponseResult.okResult();
    }

    //@Override
    //public ResponseResult deleteTag(Long id) {
    //    // 逻辑删除，将逻辑删除字段改为1
    //    UpdateWrapper<Tag> updateWrapper = new UpdateWrapper<>();
    //    updateWrapper.eq("id", id);
    //    updateWrapper.set("del_flag", SystemConstants.IS_DELETED_TRUE);
    //
    //    update(updateWrapper);
    //
    //    return ResponseResult.okResult();
    //}

    @Override
    public ResponseResult getTag(Long id) {
        Tag tag = getById(id);
        if (tag == null) {
            throw new SystemException(AppHttpCodeEnum.TAG_NOT_EXIST);
        }

        return ResponseResult.okResult(tag);
    }

    @Override
    public ResponseResult updateTag(TagDto tagDto) {
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        //如果标签名称被修改，则判断标签是否重复
        if (!tag.getName().equals(tagDto.getOldName())) {
            LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Tag::getName, tag.getName());
            if (getOne(queryWrapper) != null) {
                throw new SystemException(AppHttpCodeEnum.NAME_EXIST_ERROR);
            }
        }

        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<List<TagVo>> listAllTag() {
        List<Tag> tagList = list();
        List<TagVo> tagVoList = BeanCopyUtils.copyBeanList(tagList, TagVo.class);
        return ResponseResult.okResult(tagVoList);
    }

    @Override
    public ResponseResult deleteBatchTag(List<Long> ids) {
        // 逻辑删除标签，将逻辑删除字段改为1
        UpdateWrapper<Tag> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", ids);
        updateWrapper.set("del_flag", SystemConstants.IS_DELETED_TRUE);

        update(updateWrapper);

        return ResponseResult.okResult();
    }
}

