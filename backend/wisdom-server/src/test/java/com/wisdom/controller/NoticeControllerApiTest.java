package com.wisdom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.dto.NoticeDTO;
import com.wisdom.dto.NoticePageQueryDTO;
import com.wisdom.exception.BusinessException;
import com.wisdom.handler.GlobalExceptionHandler;
import com.wisdom.result.PageResult;
import com.wisdom.service.NoticeService;
import com.wisdom.vo.NoticeVO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class NoticeControllerApiTest {

    @Mock
    private NoticeService noticeService;

    @InjectMocks
    private NoticeController noticeController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(noticeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getNoticeListShouldReturnPagedDataWhenQueryIsValid() throws Exception {
        NoticeVO noticeVO = new NoticeVO();
        noticeVO.setId(1L);
        noticeVO.setTitle("Water outage");
        noticeVO.setStatus("PUBLISHED");
        noticeVO.setTargetType("OWNER");
        noticeVO.setCreateTime(LocalDateTime.of(2026, 4, 26, 12, 0));

        PageResult<NoticeVO> pageResult = new PageResult<>(1, List.of(noticeVO));

        when(noticeService.getNoticeList(any(NoticePageQueryDTO.class))).thenReturn(pageResult);

        mockMvc.perform(get("/api/v1/notices")
                        .param("page", "1")
                        .param("pageSize", "10")
                        .param("title", "Water"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.records[0].title").value("Water outage"));

        ArgumentCaptor<NoticePageQueryDTO> captor = ArgumentCaptor.forClass(NoticePageQueryDTO.class);
        verify(noticeService).getNoticeList(captor.capture());
        assertEquals(1, captor.getValue().getPage());
        assertEquals(10, captor.getValue().getPageSize());
        assertEquals("Water", captor.getValue().getTitle());
    }

    @Test
    void getNoticeListShouldReturnErrorWhenServiceThrowsException() throws Exception {
        when(noticeService.getNoticeList(any(NoticePageQueryDTO.class)))
                .thenThrow(new BusinessException("NOTICE_QUERY_FAILED"));

        mockMvc.perform(get("/api/v1/notices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("NOTICE_QUERY_FAILED"));
    }

    @Test
    void saveNoticeShouldReturnSuccessWhenPayloadIsValid() throws Exception {
        NoticeDTO noticeDTO = new NoticeDTO();
        noticeDTO.setTitle("Elevator maintenance");
        noticeDTO.setContent("Maintenance will start tomorrow.");
        noticeDTO.setTargetType("OWNER");
        noticeDTO.setStatus("DRAFT");

        doNothing().when(noticeService).saveNotice(any(NoticeDTO.class));

        mockMvc.perform(post("/api/v1/notices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noticeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("success"));
    }

    @Test
    void saveNoticeShouldReturnErrorWhenServiceThrowsException() throws Exception {
        NoticeDTO noticeDTO = new NoticeDTO();
        noticeDTO.setTitle("Elevator maintenance");
        noticeDTO.setContent("Maintenance will start tomorrow.");

        doThrow(new BusinessException("NOTICE_SAVE_FAILED"))
                .when(noticeService)
                .saveNotice(any(NoticeDTO.class));

        mockMvc.perform(post("/api/v1/notices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noticeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("NOTICE_SAVE_FAILED"));
    }
}
