package com.xiaohai.domain.dto;
import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 评论表(Comment)表实体类
 *
 * @author makejava
 * @since 2024-05-09 10:39:39
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Comment对象", description = "评论表")
public class CommentDto {
    private Long id;
    //评论类型（0代表文章评论，1代表友链评论）
    @ApiModelProperty(value = "评论类型（0代表文章评论，1代表友链评论）")
    private String type;
    //文章id
    private Long articleId;
    //根评论id
    @ApiModelProperty(value = "根评论id")
    private Long rootId;
    //评论内容
    @ApiModelProperty(value = "评论内容")
    private String content;
    //所回复的目标评论的userid
    @ApiModelProperty(value = "所回复的目标评论的userid")
    private Long toCommentUserId;
    //回复目标评论id
    @ApiModelProperty(value = "回复目标评论id")
    private Long toCommentId;
    @ApiModelProperty(value = "创建者")
    private Long createBy;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新者")
    private Long updateBy;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    @ApiModelProperty(value = "删除标志（0代表未删除，1代表已删除）")
    private Integer delFlag;
}

