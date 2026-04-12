package com.wisdom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.dto.NoticeDTO;
import com.wisdom.dto.NoticePageQueryDTO;
import com.wisdom.entity.Notice;
import com.wisdom.mapper.NoticeMapper;
import com.wisdom.result.PageResult;
import com.wisdom.service.NoticeService;
import com.wisdom.vo.NoticeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public PageResult<NoticeVO> getNoticeList(NoticePageQueryDTO noticePageQueryDTO) {
        Page<Notice> page = new Page<>(noticePageQueryDTO.getPage(), noticePageQueryDTO.getPageSize());
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<>();
        if (noticePageQueryDTO.getTitle() != null) {
            queryWrapper.like(Notice::getTitle, noticePageQueryDTO.getTitle());
        }
        if (noticePageQueryDTO.getStatus() != null) {
            queryWrapper.eq(Notice::getStatus, noticePageQueryDTO.getStatus());
        }
        if (noticePageQueryDTO.getTargetType() != null) {
            queryWrapper.eq(Notice::getTargetType, noticePageQueryDTO.getTargetType());
        }
        IPage<Notice> noticePage = noticeMapper.selectPage(page, queryWrapper);
        List<NoticeVO> noticeVOList = noticePage.getRecords().stream()
                .map(notice -> {
                    NoticeVO noticeVO = new NoticeVO();
                    BeanUtils.copyProperties(notice, noticeVO);
                    return noticeVO;
                })
                .collect(Collectors.toList());
        return new PageResult<>(noticePage.getTotal(), noticeVOList);
    }

    @Override
    public void saveNotice(NoticeDTO noticeDTO) {
        Notice notice = new Notice();
        BeanUtils.copyProperties(noticeDTO, notice);
        if (noticeDTO.getId() == null) {
            notice.setViewCount(0);
            notice.setCreateTime(LocalDateTime.now());
            noticeMapper.insert(notice);
        } else {
            noticeMapper.updateById(notice);
        }
    }
}