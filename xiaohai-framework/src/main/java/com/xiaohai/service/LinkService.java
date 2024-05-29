package com.xiaohai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.dto.LinkListDto;
import com.xiaohai.domain.entity.Link;
import com.xiaohai.domain.vo.PageVo;

import java.util.List;

/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2024-05-07 18:33:06
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult<PageVo> listPage(Integer pageNum, Integer pageSize, LinkListDto linkListDto);

    ResponseResult addLink(Link link);

    ResponseResult<Link> getLink(Long id);

    ResponseResult updateLink(Link link);

    ResponseResult deleteBatchLink(List<Long> ids);
}

