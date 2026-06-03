package com.wisdom.service.impl;

import com.wisdom.ai.AIService;
import com.wisdom.ai.RepairAnalysisResult;
import com.wisdom.context.BaseContext;
import com.wisdom.dto.RepairDTO;
import com.wisdom.dto.RepairDispatchDTO;
import com.wisdom.dto.RepairStatusUpdateDTO;
import com.wisdom.entity.House;
import com.wisdom.entity.Repair;
import com.wisdom.entity.User;
import com.wisdom.exception.BusinessException;
import com.wisdom.mapper.AssetMapper;
import com.wisdom.mapper.RepairMapper;
import com.wisdom.mapper.UserMapper;
import com.wisdom.service.UserService;
import com.wisdom.vo.RepairKanbanVO;
import com.wisdom.vo.RepairVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepairServiceImplTest {

    @Mock
    private RepairMapper repairMapper;

    @Mock
    private AssetMapper assetMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AIService aiService;

    @Mock
    private UserService userService;

    @InjectMocks
    private RepairServiceImpl repairService;

    private Repair createRepair(Long id, int status, int priority, Long houseId, Long reporterId, String reporter) {
        Repair repair = new Repair();
        repair.setId(id);
        repair.setRepairNo("REP-" + id);
        repair.setHouseId(houseId);
        repair.setContent("故障描述" + id);
        repair.setReporter(reporter);
        repair.setReporterId(reporterId);
        repair.setWorkerId(null);
        repair.setStatus(status);
        repair.setPriority(priority);
        return repair;
    }

    @Test
    void getRepairKanbanShouldGroupByStatusWhenAdmin() {
        Repair pending = createRepair(1L, 0, 1, 100L, 10L, "业主A");
        Repair processing = createRepair(2L, 1, 2, 101L, 11L, "业主B");
        Repair completed = createRepair(3L, 2, 1, 102L, 12L, "业主C");

        when(userService.isCurrentUserAdmin()).thenReturn(true);
        when(repairMapper.selectList(null)).thenReturn(List.of(pending, processing, completed));
        when(assetMapper.selectById(anyLong())).thenReturn(null);
        when(userMapper.selectById(anyLong())).thenReturn(null);

        RepairKanbanVO kanban = repairService.getRepairKanban();

        assertEquals(1, kanban.getPending().size());
        assertEquals(1, kanban.getProcessing().size());
        assertEquals(1, kanban.getCompleted().size());
    }

    @Test
    void getRepairKanbanShouldFilterByReporterWhenNonAdmin() {
        Repair ownRepair = createRepair(1L, 0, 1, 100L, 10L, "业主A");
        Repair otherRepair = createRepair(2L, 1, 2, 101L, 99L, "业主B");

        when(userService.isCurrentUserAdmin()).thenReturn(false);
        when(userService.getRequiredCurrentUserId()).thenReturn(10L);
        when(repairMapper.selectList(null)).thenReturn(List.of(ownRepair, otherRepair));
        when(assetMapper.selectById(anyLong())).thenReturn(null);
        when(userMapper.selectById(anyLong())).thenReturn(null);

        RepairKanbanVO kanban = repairService.getRepairKanban();

        assertEquals(1, kanban.getPending().size());
        assertEquals(0, kanban.getProcessing().size());
        assertEquals(0, kanban.getCompleted().size());
    }

    @Test
    void getRepairKanbanShouldFilterByReporterNameWhenAdminWithParam() {
        Repair repairA = createRepair(1L, 0, 1, 100L, 10L, "张三");
        Repair repairB = createRepair(2L, 1, 1, 101L, 11L, "李四");

        when(userService.isCurrentUserAdmin()).thenReturn(true);
        when(repairMapper.selectList(null)).thenReturn(List.of(repairA, repairB));
        when(assetMapper.selectById(anyLong())).thenReturn(null);
        when(userMapper.selectById(anyLong())).thenReturn(null);

        RepairKanbanVO kanban = repairService.getRepairKanban("李四");

        assertEquals(0, kanban.getPending().size());
        assertEquals(1, kanban.getProcessing().size());
        assertEquals(0, kanban.getCompleted().size());
    }

    @Test
    void getRepairKanbanShouldEnrichWithHouseAndWorkerNames() {
        Repair repair = createRepair(1L, 0, 1, 100L, 10L, "业主A");
        repair.setWorkerId(5L);

        House house = new House();
        house.setId(100L);
        house.setName("1栋101");
        User worker = new User();
        worker.setId(5L);
        worker.setUsername("维修工张三");

        when(userService.isCurrentUserAdmin()).thenReturn(true);
        when(repairMapper.selectList(null)).thenReturn(List.of(repair));
        when(assetMapper.selectById(100L)).thenReturn(house);
        when(userMapper.selectById(5L)).thenReturn(worker);

        RepairKanbanVO kanban = repairService.getRepairKanban();

        RepairVO vo = kanban.getPending().get(0);
        assertEquals("1栋101", vo.getHouseName());
        assertEquals("维修工张三", vo.getWorkerName());
    }

    @Test
    void addRepairShouldCreateRepairWhenAdmin() {
        BaseContext.setCurrentId(1L);

        RepairDTO dto = new RepairDTO();
        dto.setHouseId(100L);
        dto.setContent("漏水需要维修");
        dto.setReporter("管理员");

        RepairAnalysisResult aiResult = new RepairAnalysisResult();
        aiResult.setPriority(2);
        aiResult.setPriorityText("紧急");
        aiResult.setSuggestion("立即处理");

        when(userService.isCurrentUserAdmin()).thenReturn(true);
        when(aiService.analyzeRepair("漏水需要维修")).thenReturn(aiResult);

        repairService.addRepair(dto);

        ArgumentCaptor<Repair> captor = ArgumentCaptor.forClass(Repair.class);
        verify(repairMapper).insert(captor.capture());
        assertEquals(100L, captor.getValue().getHouseId());
        assertEquals(1L, captor.getValue().getReporterId());
        assertEquals(2, captor.getValue().getPriority());
        assertEquals(0, captor.getValue().getStatus());
        assertNotNull(captor.getValue().getRepairNo());

        BaseContext.removeCurrentId();
    }

    @Test
    void addRepairShouldEnforceOwnershipWhenNonAdmin() {
        BaseContext.setCurrentId(10L);

        RepairDTO dto = new RepairDTO();
        dto.setHouseId(200L);

        when(userService.isCurrentUserAdmin()).thenReturn(false);
        when(userService.getRequiredCurrentUserId()).thenReturn(10L);
        when(userService.getOwnedHouseIds(10L)).thenReturn(List.of(100L, 101L));

        assertThrows(BusinessException.class, () -> repairService.addRepair(dto));

        verify(repairMapper, never()).insert(any(Repair.class));

        BaseContext.removeCurrentId();
    }

    @Test
    void addRepairShouldAllowOwnedHouseWhenNonAdmin() {
        BaseContext.setCurrentId(10L);

        RepairDTO dto = new RepairDTO();
        dto.setHouseId(100L);
        dto.setContent("修水管");

        RepairAnalysisResult aiResult = new RepairAnalysisResult();
        aiResult.setPriority(1);

        when(userService.isCurrentUserAdmin()).thenReturn(false);
        when(userService.getRequiredCurrentUserId()).thenReturn(10L);
        when(userService.getOwnedHouseIds(10L)).thenReturn(List.of(100L, 101L));
        when(aiService.analyzeRepair("修水管")).thenReturn(aiResult);

        repairService.addRepair(dto);

        verify(repairMapper).insert(any(Repair.class));

        BaseContext.removeCurrentId();
    }

    @Test
    void dispatchRepairShouldAssignWorker() {
        RepairDispatchDTO dto = new RepairDispatchDTO();
        dto.setId(1L);
        dto.setWorkerId(5L);

        Repair repair = new Repair();
        repair.setId(1L);
        repair.setStatus(0);

        when(repairMapper.selectById(1L)).thenReturn(repair);

        repairService.dispatchRepair(dto);

        ArgumentCaptor<Repair> captor = ArgumentCaptor.forClass(Repair.class);
        verify(repairMapper).updateById(captor.capture());
        assertEquals(5L, captor.getValue().getWorkerId());
        assertEquals(1, captor.getValue().getStatus());
    }

    @Test
    void dispatchRepairShouldThrowWhenNotFound() {
        RepairDispatchDTO dto = new RepairDispatchDTO();
        dto.setId(404L);
        dto.setWorkerId(5L);

        when(repairMapper.selectById(404L)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> repairService.dispatchRepair(dto));

        verify(repairMapper, never()).updateById(any(Repair.class));
    }

    @Test
    void updateRepairStatusShouldSetFinishTimeWhenCompleted() {
        RepairStatusUpdateDTO dto = new RepairStatusUpdateDTO();
        dto.setId(1L);
        dto.setStatus(2);

        Repair repair = new Repair();
        repair.setId(1L);
        repair.setStatus(1);

        when(repairMapper.selectById(1L)).thenReturn(repair);

        repairService.updateRepairStatus(dto);

        ArgumentCaptor<Repair> captor = ArgumentCaptor.forClass(Repair.class);
        verify(repairMapper).updateById(captor.capture());
        assertEquals(2, captor.getValue().getStatus());
        assertNotNull(captor.getValue().getFinishTime());
    }

    @Test
    void updateRepairStatusShouldNotSetFinishTimeWhenNotCompleted() {
        RepairStatusUpdateDTO dto = new RepairStatusUpdateDTO();
        dto.setId(1L);
        dto.setStatus(1);

        Repair repair = new Repair();
        repair.setId(1L);

        when(repairMapper.selectById(1L)).thenReturn(repair);

        repairService.updateRepairStatus(dto);

        ArgumentCaptor<Repair> captor = ArgumentCaptor.forClass(Repair.class);
        verify(repairMapper).updateById(captor.capture());
        assertEquals(1, captor.getValue().getStatus());
    }

    @Test
    void updateRepairStatusShouldThrowWhenNotFound() {
        RepairStatusUpdateDTO dto = new RepairStatusUpdateDTO();
        dto.setId(404L);
        dto.setStatus(1);

        when(repairMapper.selectById(404L)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> repairService.updateRepairStatus(dto));
    }
}
