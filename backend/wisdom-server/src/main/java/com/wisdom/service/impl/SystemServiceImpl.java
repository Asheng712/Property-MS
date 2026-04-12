package com.wisdom.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.dto.PageQueryDTO;
import com.wisdom.dto.ReportExportDTO;
import com.wisdom.entity.FileTask;
import com.wisdom.mapper.FileTaskMapper;
import com.wisdom.result.PageResult;
import com.wisdom.service.SystemService;
import com.wisdom.vo.FileTaskVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SystemServiceImpl implements SystemService {

    @Autowired
    private FileTaskMapper fileTaskMapper;

    @Override
    public PageResult<FileTaskVO> getFileTasks(PageQueryDTO pageQueryDTO) {
        Page<FileTask> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        IPage<FileTask> fileTaskPage = fileTaskMapper.selectPage(page, null);
        List<FileTaskVO> fileTaskVOList = fileTaskPage.getRecords().stream()
                .map(fileTask -> {
                    FileTaskVO fileTaskVO = new FileTaskVO();
                    BeanUtils.copyProperties(fileTask, fileTaskVO);
                    return fileTaskVO;
                })
                .collect(Collectors.toList());
        return new PageResult<>(fileTaskPage.getTotal(), fileTaskVOList);
    }

    @Override
    public void exportFinanceReport(ReportExportDTO reportExportDTO) {
        FileTask fileTask = new FileTask();
        fileTask.setTaskType("EXPORT");
        fileTask.setFileName("财务报表_" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")) + ".xlsx");
        fileTask.setOperator("admin");
        fileTask.setStatus("PROCESSING");
        fileTask.setCreateTime(LocalDateTime.now());
        fileTaskMapper.insert(fileTask);
        
        fileTask.setStatus("SUCCESS");
        fileTask.setFileUrl("/downloads/finance_report.xlsx");
        fileTaskMapper.updateById(fileTask);
    }

    @Override
    public void importAssets(MultipartFile file) {
        FileTask fileTask = new FileTask();
        fileTask.setTaskType("IMPORT");
        fileTask.setFileName(file.getOriginalFilename());
        fileTask.setOperator("admin");
        fileTask.setStatus("PROCESSING");
        fileTask.setCreateTime(LocalDateTime.now());
        fileTaskMapper.insert(fileTask);
        
        fileTask.setStatus("SUCCESS");
        fileTask.setDataCount(10);
        fileTaskMapper.updateById(fileTask);
    }
}