package com.wisdom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.dto.ComplaintHandleDTO;
import com.wisdom.dto.ComplaintPageQueryDTO;
import com.wisdom.entity.Complaint;
import com.wisdom.mapper.ComplaintMapper;
import com.wisdom.result.PageResult;
import com.wisdom.vo.ComplaintVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComplaintServiceImplTest {

    @Mock
    private ComplaintMapper complaintMapper;

    @InjectMocks
    private ComplaintServiceImpl complaintService;

    @Test
    void getComplaintListShouldReturnMappedPageWhenFiltersAreProvided() {
        ComplaintPageQueryDTO dto = new ComplaintPageQueryDTO();
        dto.setPage(3);
        dto.setPageSize(4);
        dto.setCategory("环境");
        dto.setStatus(2);

        Complaint complaint = new Complaint();
        complaint.setId(11L);
        complaint.setComplaintNo("TS20260426001");
        complaint.setCategory("环境");
        complaint.setContent("楼道卫生需要处理");
        complaint.setSource("业主APP");
        complaint.setStatus(2);

        Page<Complaint> page = new Page<>(3, 4);
        page.setRecords(List.of(complaint));
        page.setTotal(1);

        when(complaintMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);

        PageResult<ComplaintVO> result = complaintService.getComplaintList(dto);

        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        ComplaintVO vo = result.getRecords().get(0);
        assertEquals(11L, vo.getId());
        assertEquals("TS20260426001", vo.getComplaintNo());
        assertEquals("环境", vo.getCategory());
        assertEquals("楼道卫生需要处理", vo.getContent());
        assertEquals("业主APP", vo.getSource());
        assertEquals(2, vo.getStatus());

        ArgumentCaptor<Page<Complaint>> pageCaptor = ArgumentCaptor.forClass(Page.class);
        verify(complaintMapper).selectPage(pageCaptor.capture(), any(LambdaQueryWrapper.class));
        assertEquals(3, pageCaptor.getValue().getCurrent());
        assertEquals(4, pageCaptor.getValue().getSize());
    }

    @Test
    void getComplaintListShouldUseDefaultPageWhenPaginationIsMissing() {
        ComplaintPageQueryDTO dto = new ComplaintPageQueryDTO();
        Page<Complaint> page = new Page<>(1, 10);
        page.setRecords(List.of());

        when(complaintMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);

        PageResult<ComplaintVO> result = complaintService.getComplaintList(dto);

        assertEquals(0, result.getTotal());
        assertEquals(0, result.getRecords().size());

        ArgumentCaptor<Page<Complaint>> pageCaptor = ArgumentCaptor.forClass(Page.class);
        verify(complaintMapper).selectPage(pageCaptor.capture(), any(LambdaQueryWrapper.class));
        assertEquals(1, pageCaptor.getValue().getCurrent());
        assertEquals(10, pageCaptor.getValue().getSize());
    }

    @Test
    void handleComplaintShouldUpdateStatusWhenComplaintExists() {
        ComplaintHandleDTO dto = new ComplaintHandleDTO();
        dto.setId(8L);
        dto.setStatus(2);

        Complaint complaint = new Complaint();
        complaint.setId(8L);
        complaint.setStatus(0);

        when(complaintMapper.selectById(8L)).thenReturn(complaint);

        complaintService.handleComplaint(dto);

        ArgumentCaptor<Complaint> captor = ArgumentCaptor.forClass(Complaint.class);
        verify(complaintMapper, times(1)).updateById(captor.capture());
        assertEquals(8L, captor.getValue().getId());
        assertEquals(2, captor.getValue().getStatus());
    }

    @Test
    void handleComplaintShouldThrowWhenComplaintDoesNotExist() {
        ComplaintHandleDTO dto = new ComplaintHandleDTO();
        dto.setId(404L);
        dto.setStatus(1);

        when(complaintMapper.selectById(404L)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> complaintService.handleComplaint(dto));

        verify(complaintMapper, never()).updateById(any(Complaint.class));
    }
}
