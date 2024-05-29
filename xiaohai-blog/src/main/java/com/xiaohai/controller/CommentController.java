package com.xiaohai.controller;

import com.xiaohai.constants.SystemConstants;
import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.dto.CommentDto;
import com.xiaohai.domain.entity.Comment;
import com.xiaohai.service.CommentService;
import com.xiaohai.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论模块")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @GetMapping("/commentList")
    @ApiOperation(value = "评论列表", notes = "展示所有评论")
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
    }

    @PostMapping
    @ApiOperation(value = "添加评论", notes = "用户添加评论")
    public ResponseResult addComment(@RequestBody CommentDto commentDto){
        Comment comment = BeanCopyUtils.copyBean(commentDto, Comment.class);
        return commentService.addComment(comment);
    }

    @GetMapping("/linkCommentList")
    @ApiOperation(value = "友链评论列表", notes = "展示所有友链评论")
    public ResponseResult LinkCommentList(Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }
}
