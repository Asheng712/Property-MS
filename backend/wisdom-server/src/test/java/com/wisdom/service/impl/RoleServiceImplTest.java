package com.wisdom.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.dto.PageQueryDTO;
import com.wisdom.dto.RolePermUpdateDTO;
import com.wisdom.entity.Role;
import com.wisdom.mapper.RoleMapper;
import com.wisdom.result.PageResult;
import com.wisdom.vo.RoleVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void getRoleListShouldReturnMappedPageWhenDataExists() {
        PageQueryDTO dto = new PageQueryDTO();
        dto.setPage(1);
        dto.setPageSize(5);

        Role role = new Role();
        role.setId(1L);
        role.setRoleName("管理员");
        role.setRoleKey("ADMIN");
        role.setPermissions("ALL");

        Page<Role> page = new Page<>(1, 5);
        page.setRecords(List.of(role));
        page.setTotal(1);

        when(roleMapper.selectPage(any(Page.class), any())).thenReturn(page);

        PageResult<RoleVO> result = roleService.getRoleList(dto);

        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        assertEquals("管理员", result.getRecords().get(0).getRoleName());
        assertEquals("ADMIN", result.getRecords().get(0).getRoleKey());
    }

    @Test
    void getRoleListShouldReturnEmptyWhenNoData() {
        PageQueryDTO dto = new PageQueryDTO();
        Page<Role> page = new Page<>(1, 10);
        page.setRecords(List.of());

        when(roleMapper.selectPage(any(Page.class), any())).thenReturn(page);

        PageResult<RoleVO> result = roleService.getRoleList(dto);

        assertEquals(0, result.getTotal());
        assertEquals(0, result.getRecords().size());
    }

    @Test
    void getRoleListShouldUseDefaultPageWhenPaginationMissing() {
        PageQueryDTO dto = new PageQueryDTO();
        Page<Role> page = new Page<>(1, 10);
        page.setRecords(List.of());

        when(roleMapper.selectPage(any(Page.class), any())).thenReturn(page);

        roleService.getRoleList(dto);

        ArgumentCaptor<Page<Role>> captor = ArgumentCaptor.forClass(Page.class);
        verify(roleMapper).selectPage(captor.capture(), any());
        assertEquals(1, captor.getValue().getCurrent());
        assertEquals(10, captor.getValue().getSize());
    }

    @Test
    void updateRolePermsShouldUpdateWhenRoleExists() {
        RolePermUpdateDTO dto = new RolePermUpdateDTO();
        dto.setId(2L);
        dto.setPermissions("READ,WRITE");

        Role role = new Role();
        role.setId(2L);
        role.setRoleName("普通用户");
        role.setPermissions("READ");

        when(roleMapper.selectById(2L)).thenReturn(role);

        roleService.updateRolePerms(dto);

        ArgumentCaptor<Role> captor = ArgumentCaptor.forClass(Role.class);
        verify(roleMapper).updateById(captor.capture());
        assertEquals(2L, captor.getValue().getId());
        assertEquals("READ,WRITE", captor.getValue().getPermissions());
    }

    @Test
    void updateRolePermsShouldThrowWhenRoleNotFound() {
        RolePermUpdateDTO dto = new RolePermUpdateDTO();
        dto.setId(404L);
        dto.setPermissions("READ");

        when(roleMapper.selectById(404L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> roleService.updateRolePerms(dto));

        assertEquals("角色不存在", ex.getMessage());
        verify(roleMapper, never()).updateById(any(Role.class));
    }
}
