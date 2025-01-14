package com.xiaohai.domain.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleListVo {
    private Long id;
    //标题
    private String title;
    //文章摘要
    private String summary;
    //所属分类name
    private String categoryName;
    //缩略图
    private String thumbnail;
    //访问量
    private Long viewCount;
    private Date createTime;
}


