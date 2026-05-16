package com.wisdom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.dto.ComplaintHandleDTO;
import com.wisdom.dto.ComplaintPageQueryDTO;
import com.wisdom.exception.BusinessException;
import com.wisdom.handler.GlobalExceptionHandler;
import com.wisdom.result.PageResult;
import com.wisdom.service.ComplaintService;
import com.wisdom.vo.ComplaintVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ComplaintControllerApiTest {

    @Mock
    private ComplaintService complaintService;

    @InjectMocks
    private ComplaintController complaintController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(complaintController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getComplaintListShouldReturnPagedDataWhenQueryIsValid() throws Exception {
        ComplaintVO complaintVO = new ComplaintVO();
        complaintVO.setId(1L);
        complaintVO.setComplaintNo("C20260426001");
        complaintVO.setCategory("Noise");
        complaintVO.setContent("Too loud at midnight");
        complaintVO.setStatus(0);
        complaintVO.setStatusText("PENDING");
        complaintVO.setCreateTime(LocalDateTime.of(2026, 4, 26, 12, 30));

        PageResult<ComplaintVO> pageResult = new PageResult<>(1, List.of(complaintVO));

        when(complaintService.getComplaintList(any(ComplaintPageQueryDTO.class))).thenReturn(pageResult);

        mockMvc.perform(get("/api/v1/complaints")
                        .param("page", "2")
                        .param("pageSize", "5")
                        .param("category", "Noise")
                        .param("status", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.records[0].complaintNo").value("C20260426001"));

        ArgumentCaptor<ComplaintPageQueryDTO> captor = ArgumentCaptor.forClass(ComplaintPageQueryDTO.class);
        verify(complaintService).getComplaintList(captor.capture());
        assertEquals(2, captor.getValue().getPage());
        assertEquals(5, captor.getValue().getPageSize());
        assertEquals("Noise", captor.getValue().getCategory());
        assertEquals(0, captor.getValue().getStatus());
    }

    @Test
    void getComplaintListShouldReturnErrorWhenServiceThrowsException() throws Exception {
        when(complaintService.getComplaintList(any(ComplaintPageQueryDTO.class)))
                .thenThrow(new BusinessException("COMPLAINT_QUERY_FAILED"));

        mockMvc.perform(get("/api/v1/complaints"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("COMPLAINT_QUERY_FAILED"));
    }

    @Test
    void handleComplaintShouldReturnSuccessWhenPayloadIsValid() throws Exception {
        ComplaintHandleDTO complaintHandleDTO = new ComplaintHandleDTO();
        complaintHandleDTO.setId(8L);
        complaintHandleDTO.setStatus(1);
        complaintHandleDTO.setHandleResult("Assigned to property manager");

        doNothing().when(complaintService).handleComplaint(any(ComplaintHandleDTO.class));

        mockMvc.perform(put("/api/v1/complaints/handle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(complaintHandleDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("success"));
    }

    @Test
    void handleComplaintShouldReturnErrorWhenServiceThrowsException() throws Exception {
        ComplaintHandleDTO complaintHandleDTO = new ComplaintHandleDTO();
        complaintHandleDTO.setId(8L);
        complaintHandleDTO.setStatus(2);

        doThrow(new BusinessException("COMPLAINT_HANDLE_FAILED"))
                .when(complaintService)
                .handleComplaint(any(ComplaintHandleDTO.class));

        mockMvc.perform(put("/api/v1/complaints/handle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(complaintHandleDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("COMPLAINT_HANDLE_FAILED"));
    }
}
