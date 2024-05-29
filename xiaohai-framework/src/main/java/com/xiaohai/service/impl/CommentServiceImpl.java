package com.xiaohai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohai.constants.SystemConstants;
import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.entity.Comment;
import com.xiaohai.domain.vo.CommentVo;
import com.xiaohai.domain.vo.PageVo;
import com.xiaohai.enums.AppHttpCodeEnum;
import com.xiaohai.exception.SystemException;
import com.xiaohai.mapper.CommentMapper;
import com.xiaohai.service.CommentService;
import com.xiaohai.service.UserService;
import com.xiaohai.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2024-05-09 10:39:41
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;
    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        // 查询对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        // 对articleId 进行判断
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId, articleId);
        // 根评论 rootId 为 -1
        queryWrapper.eq(Comment::getRootId, -1);
        // 评论类型
        queryWrapper.eq(Comment::getType, commentType);
        // 分页查询
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        // 封装VO
        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());

        // 查询对应文章的子评论
        for (CommentVo commentVo : commentVoList) {
            // 查询子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            // 赋值
            commentVo.setChildren(children);
        }
        return ResponseResult.okResult(new PageVo(commentVoList, page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        // 内容不能为空
        if (!StringUtils.hasText(comment.getContent())) {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }


    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId, id);
        queryWrapper.orderByAsc(Comment::getCreateTime);

        List<Comment> commentList = list(queryWrapper);
        List<CommentVo> commentVos = toCommentVoList(commentList);
        return commentVos;
    }

    private List<CommentVo> toCommentVoList(List<Comment> commentList) {
        List<CommentVo> commentVoList = BeanCopyUtils.copyBeanList(commentList, CommentVo.class);

        //遍历vo集合
        //List<CommentVo> commentVos = commentVoList.stream()
        //        .map(commentVo -> {
        //            //通过createBy查询用户的昵称并赋值
        //            Long userId = commentVo.getCreateBy();
        //            String userName = userService.getById(userId).getNickName();
        //            commentVo.setUserName(userName);
        //            //如果toCommentUserId不等于-1才进行查询
        //            if (commentVo.getToCommentUserId() != -1) {
        //                String commentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
        //                commentVo.setToCommentUserName(commentUserName);
        //            }
        //            return commentVo;
        //        })
        //        .collect(Collectors.toList());
        for (CommentVo commentVo : commentVoList) {
            //通过createBy查询用户的昵称并赋值
            Long userId = commentVo.getCreateBy();
            String userName = userService.getById(userId).getNickName();
            commentVo.setUserName(userName);

            //如果toCommentUserId不等于-1才进行查询
            if (commentVo.getToCommentUserId() != -1) {
                String commentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(commentUserName);
            }
        }
        return commentVoList;
    }
}

