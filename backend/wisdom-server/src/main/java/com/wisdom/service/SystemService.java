package com.wisdom.service;

import com.wisdom.dto.PageQueryDTO;
import com.wisdom.dto.ReportExportDTO;
import com.wisdom.result.PageResult;
import com.wisdom.vo.FileTaskVO;
import org.springframework.web.multipart.MultipartFile;

public interface SystemService {
    PageResult<FileTaskVO> getFileTasks(PageQueryDTO pageQueryDTO);
    void exportFinanceReport(ReportExportDTO reportExportDTO);
    void importAssets(MultipartFile file);
}