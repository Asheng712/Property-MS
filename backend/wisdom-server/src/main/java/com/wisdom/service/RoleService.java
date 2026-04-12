package com.wisdom.service;

import com.wisdom.dto.PageQueryDTO;
import com.wisdom.dto.RolePermUpdateDTO;
import com.wisdom.result.PageResult;
import com.wisdom.vo.RoleVO;

public interface RoleService {
    PageResult<RoleVO> getRoleList(PageQueryDTO pageQueryDTO);
    void updateRolePerms(RolePermUpdateDTO rolePermUpdateDTO);
}