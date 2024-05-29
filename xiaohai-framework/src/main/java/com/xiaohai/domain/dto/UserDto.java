package com.xiaohai.domain.dto;

import com.xiaohai.domain.entity.Role;
import com.xiaohai.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private List<Long> roleIds;
    private List<Role> roles;
    private User user;
}
