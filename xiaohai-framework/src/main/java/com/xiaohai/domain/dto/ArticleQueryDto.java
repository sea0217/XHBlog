package com.xiaohai.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleQueryDto {
    //标题
    private String title;
    //文章摘要
    private String summary;
    //状态（0已发布，1草稿）
    private String status;
}
