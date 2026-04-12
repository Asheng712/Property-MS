package com.wisdom.vo;

import lombok.Data;

@Data
public class RoleVO {
    private Long id;
    private String roleName;
    private String roleKey;
    private String permissions;
}