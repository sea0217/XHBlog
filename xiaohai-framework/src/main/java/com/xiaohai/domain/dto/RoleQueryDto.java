package com.xiaohai.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleQueryDto {
    private Long roleId;
    private String roleName;
    //角色状态（0正常 1停用）
    private String status;
}
