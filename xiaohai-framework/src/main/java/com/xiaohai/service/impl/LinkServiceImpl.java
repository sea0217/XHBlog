package com.xiaohai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohai.constants.SystemConstants;
import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.dto.LinkListDto;
import com.xiaohai.domain.entity.Link;
import com.xiaohai.domain.vo.LinkVo;
import com.xiaohai.domain.vo.PageVo;
import com.xiaohai.enums.AppHttpCodeEnum;
import com.xiaohai.exception.SystemException;
import com.xiaohai.mapper.LinkMapper;
import com.xiaohai.service.LinkService;
import com.xiaohai.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2024-05-07 18:33:06
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        // 查询所有审核通过的友链
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(queryWrapper);

        // 转换成VO
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);

        // 返回结果
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult<PageVo> listPage(Integer pageNum, Integer pageSize, LinkListDto linkListDto) {
        // 分页查询
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(linkListDto.getName()), Link::getName, linkListDto.getName());
        queryWrapper.eq(StringUtils.hasText(linkListDto.getStatus()), Link::getStatus, linkListDto.getStatus());

        Page<Link> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        return ResponseResult.okResult(new PageVo(page.getRecords(), page.getTotal()));
    }

    @Override
    public ResponseResult addLink(Link link) {
        // 判断链接是否存在
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getAddress, link.getAddress());
        Link one = getOne(queryWrapper);
        if (one != null) {
            throw new SystemException(AppHttpCodeEnum.LINK_EXISTED);
        }

        // 添加
        save(link);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<Link> getLink(Long id) {

        Link link = getById(id);
        if (link == null) {
            throw new SystemException(AppHttpCodeEnum.LINK_NOT_FOUND);
        }

        return ResponseResult.okResult(link);
    }

    @Override
    public ResponseResult updateLink(Link link) {
        // 如果回显后的链接被修改，则判断修改后的链接是否存在
        if (link.getId() != null && !link.getAddress().equals(link.getAddress())){
            LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Link::getAddress, link.getAddress());
            Link one = getOne(queryWrapper);
            if (one != null) {
                throw new SystemException(AppHttpCodeEnum.LINK_EXISTED);
            }
        }


        updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteBatchLink(List<Long> ids) {
        // 逻辑删除友链
        UpdateWrapper<Link> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("del_flag", SystemConstants.IS_DELETED_TRUE);
        updateWrapper.in("id", ids);

        update(updateWrapper);

        return ResponseResult.okResult();
    }
}

