package com.xiaohai.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto {

    private Long id;
    private String name;
    private String remark;

    public String getOldName() {
        // 返回旧名称
        return name;
    }
}
