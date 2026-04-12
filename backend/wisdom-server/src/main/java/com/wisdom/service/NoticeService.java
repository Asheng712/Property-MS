package com.wisdom.service;

import com.wisdom.dto.NoticeDTO;
import com.wisdom.dto.NoticePageQueryDTO;
import com.wisdom.result.PageResult;
import com.wisdom.vo.NoticeVO;

public interface NoticeService {
    PageResult<NoticeVO> getNoticeList(NoticePageQueryDTO noticePageQueryDTO);
    void saveNotice(NoticeDTO noticeDTO);
}