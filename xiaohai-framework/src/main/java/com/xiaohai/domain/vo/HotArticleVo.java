package com.xiaohai.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotArticleVo {
    // 文章id
    private Long id;
    // 文章标题
    private String title;
    // 浏览量
    private Long viewCount;
}
