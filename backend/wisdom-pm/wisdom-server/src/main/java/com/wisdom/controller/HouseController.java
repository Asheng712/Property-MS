package com.wisdom.controller;

import com.wisdom.dto.AssetPageQueryDTO;
import com.wisdom.entity.House;
import com.wisdom.result.PageResult;
import com.wisdom.result.Result;
import com.wisdom.service.HouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/assets")
@Tag(name = "资产管理")
@Slf4j
public class HouseController {

    @Autowired
    private HouseService houseService;

    @GetMapping("/page")
    @Operation(summary = "资产分页查询")
    public Result<PageResult> page(AssetPageQueryDTO dto) {
        log.info("分页查询资产: {}", dto);
        PageResult pageResult = houseService.pageQuery(dto);
        return Result.success(pageResult);
    }

    @PostMapping
    @Operation(summary = "新增资产")
    public Result save(@RequestBody House house) {
        houseService.save(house);
        return Result.success(null);
    }

}