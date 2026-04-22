package com.wisdom.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.dto.PageQueryDTO;
import com.wisdom.dto.RolePermUpdateDTO;
import com.wisdom.entity.Role;
import com.wisdom.mapper.RoleMapper;
import com.wisdom.result.PageResult;
import com.wisdom.service.RoleService;
import com.wisdom.vo.RoleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public PageResult<RoleVO> getRoleList(PageQueryDTO pageQueryDTO) {
        Page<Role> page = new Page<>(
            pageQueryDTO.getPage() != null ? pageQueryDTO.getPage() : 1,
            pageQueryDTO.getPageSize() != null ? pageQueryDTO.getPageSize() : 10
        );
        IPage<Role> rolePage = roleMapper.selectPage(page, null);
        List<RoleVO> roleVOList = rolePage.getRecords().stream()
                .map(role -> {
                    RoleVO roleVO = new RoleVO();
                    BeanUtils.copyProperties(role, roleVO);
                    return roleVO;
                })
                .collect(Collectors.toList());
        return new PageResult<>(rolePage.getTotal(), roleVOList);
    }

    @Override
    public void updateRolePerms(RolePermUpdateDTO rolePermUpdateDTO) {
        Role role = roleMapper.selectById(rolePermUpdateDTO.getId());
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }
        role.setPermissions(rolePermUpdateDTO.getPermissions());
        roleMapper.updateById(role);
    }
}