package com.xiaohai.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuUpdateVo {
    private List<MenuVo> menus;
    private List<Long> checkedKeys;
}
