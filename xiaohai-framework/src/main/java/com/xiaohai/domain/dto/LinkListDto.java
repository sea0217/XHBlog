package com.xiaohai.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkListDto {
    //友链名
    private String name;
    //审核状态 (0代表审核通过，1代表审核未通过，2代表未审核)
    private String status;
}
