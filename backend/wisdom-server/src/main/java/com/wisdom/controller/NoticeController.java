package com.wisdom.controller;

import com.wisdom.dto.NoticeDTO;
import com.wisdom.dto.NoticePageQueryDTO;
import com.wisdom.result.Result;
import com.wisdom.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notices")
@Tag(name = "公告管理模块", description = "公告列表查询、发布等接口")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping
    @Operation(summary = "分页查询公告历史")
    public Result<?> getNoticeList(NoticePageQueryDTO noticePageQueryDTO) {
        return Result.success(noticeService.getNoticeList(noticePageQueryDTO));
    }

    @PostMapping
    @Operation(summary = "发布/暂存公告")
    public Result<Void> saveNotice(@RequestBody NoticeDTO noticeDTO) {
        noticeService.saveNotice(noticeDTO);
        return Result.success(null);
    }
}