package com.wisdom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.dto.NoticeDTO;
import com.wisdom.dto.NoticePageQueryDTO;
import com.wisdom.entity.Notice;
import com.wisdom.mapper.NoticeMapper;
import com.wisdom.result.PageResult;
import com.wisdom.vo.NoticeVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoticeServiceImplTest {

    @Mock
    private NoticeMapper noticeMapper;

    @InjectMocks
    private NoticeServiceImpl noticeService;

    @Test
    void getNoticeListShouldReturnMappedPageWhenFiltersAreProvided() {
        NoticePageQueryDTO dto = new NoticePageQueryDTO();
        dto.setPage(2);
        dto.setPageSize(5);
        dto.setTitle("停车");
        dto.setStatus("published");
        dto.setTargetType("owner");

        Notice notice = new Notice();
        notice.setId(1L);
        notice.setTitle("停车通知");
        notice.setContent("地下车库维护");
        notice.setStatus("published");
        notice.setTargetType("owner");
        notice.setViewCount(7);

        Page<Notice> page = new Page<>(2, 5);
        page.setRecords(List.of(notice));
        page.setTotal(1);

        when(noticeMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);

        PageResult<NoticeVO> result = noticeService.getNoticeList(dto);

        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        NoticeVO vo = result.getRecords().get(0);
        assertEquals(1L, vo.getId());
        assertEquals("停车通知", vo.getTitle());
        assertEquals("地下车库维护", vo.getContent());
        assertEquals("published", vo.getStatus());
        assertEquals("owner", vo.getTargetType());
        assertEquals(7, vo.getViewCount());

        ArgumentCaptor<Page<Notice>> pageCaptor = ArgumentCaptor.forClass(Page.class);
        verify(noticeMapper).selectPage(pageCaptor.capture(), any(LambdaQueryWrapper.class));
        assertEquals(2, pageCaptor.getValue().getCurrent());
        assertEquals(5, pageCaptor.getValue().getSize());
    }

    @Test
    void getNoticeListShouldUseDefaultPageWhenPaginationIsMissing() {
        NoticePageQueryDTO dto = new NoticePageQueryDTO();
        Page<Notice> page = new Page<>(1, 10);
        page.setRecords(List.of());

        when(noticeMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);

        PageResult<NoticeVO> result = noticeService.getNoticeList(dto);

        assertEquals(0, result.getTotal());
        assertEquals(0, result.getRecords().size());

        ArgumentCaptor<Page<Notice>> pageCaptor = ArgumentCaptor.forClass(Page.class);
        verify(noticeMapper).selectPage(pageCaptor.capture(), any(LambdaQueryWrapper.class));
        assertEquals(1, pageCaptor.getValue().getCurrent());
        assertEquals(10, pageCaptor.getValue().getSize());
    }

    @Test
    void saveNoticeShouldInsertNewNoticeWithInitialViewCountAndCreateTime() {
        NoticeDTO dto = new NoticeDTO();
        dto.setTitle("缴费提醒");
        dto.setContent("请按时缴纳物业费");
        dto.setStatus("draft");
        dto.setTargetType("owner");

        noticeService.saveNotice(dto);

        ArgumentCaptor<Notice> captor = ArgumentCaptor.forClass(Notice.class);
        verify(noticeMapper, times(1)).insert(captor.capture());
        verify(noticeMapper, never()).updateById(any(Notice.class));

        Notice saved = captor.getValue();
        assertEquals("缴费提醒", saved.getTitle());
        assertEquals("请按时缴纳物业费", saved.getContent());
        assertEquals("draft", saved.getStatus());
        assertEquals("owner", saved.getTargetType());
        assertEquals(0, saved.getViewCount());
        assertNotNull(saved.getCreateTime());
    }

    @Test
    void saveNoticeShouldUpdateExistingNoticeWhenIdExists() {
        NoticeDTO dto = new NoticeDTO();
        dto.setId(9L);
        dto.setTitle("更新通知");
        dto.setContent("内容已更新");
        dto.setStatus("published");
        dto.setTargetType("all");

        noticeService.saveNotice(dto);

        ArgumentCaptor<Notice> captor = ArgumentCaptor.forClass(Notice.class);
        verify(noticeMapper, times(1)).updateById(captor.capture());
        verify(noticeMapper, never()).insert(any(Notice.class));

        Notice updated = captor.getValue();
        assertEquals(9L, updated.getId());
        assertEquals("更新通知", updated.getTitle());
        assertEquals("内容已更新", updated.getContent());
        assertEquals("published", updated.getStatus());
        assertEquals("all", updated.getTargetType());
    }
}
