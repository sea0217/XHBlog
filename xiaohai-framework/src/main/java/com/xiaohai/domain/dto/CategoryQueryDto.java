package com.xiaohai.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryQueryDto {
    //分类名
    private String name;
    //状态0:正常,1禁用
    private String status;
}
