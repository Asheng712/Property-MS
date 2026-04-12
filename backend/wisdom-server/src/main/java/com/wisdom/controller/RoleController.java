package com.wisdom.controller;

import com.wisdom.dto.PageQueryDTO;
import com.wisdom.dto.RolePermUpdateDTO;
import com.wisdom.result.Result;
import com.wisdom.service.RoleService;
import com.wisdom.vo.RoleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "角色管理模块", description = "角色列表查询、角色权限更新等接口")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    @Operation(summary = "分页获取角色列表")
    public Result<?> getRoleList(PageQueryDTO pageQueryDTO) {
        return Result.success(roleService.getRoleList(pageQueryDTO));
    }

    @PutMapping("/{id}/perms")
    @Operation(summary = "更新角色权限")
    public Result<Void> updateRolePerms(@PathVariable Long id, @RequestBody RolePermUpdateDTO rolePermUpdateDTO) {
        rolePermUpdateDTO.setId(id);
        roleService.updateRolePerms(rolePermUpdateDTO);
        return Result.success(null);
    }
}