package com.wisdom.controller;

import com.wisdom.dto.AssetDTO;
import com.wisdom.dto.AssetPageQueryDTO;
import com.wisdom.result.Result;
import com.wisdom.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/assets")
@Tag(name = "资产管理模块", description = "资产空间树、资产列表、资产管理等接口")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @GetMapping("/tree")
    @Operation(summary = "获取资产空间树")
    public Result<?> getAssetTree() {
        return Result.success(assetService.getAssetTree());
    }

    @GetMapping
    @Operation(summary = "分页查询资产列表")
    public Result<?> getAssetList(AssetPageQueryDTO assetPageQueryDTO) {
        return Result.success(assetService.getAssetList(assetPageQueryDTO));
    }

    @PostMapping
    @Operation(summary = "新增资产")
    public Result<Void> addAsset(@RequestBody AssetDTO assetDTO) {
        assetService.saveOrUpdateAsset(assetDTO);
        return Result.success(null);
    }

    @PutMapping
    @Operation(summary = "编辑资产")
    public Result<Void> updateAsset(@RequestBody AssetDTO assetDTO) {
        assetService.saveOrUpdateAsset(assetDTO);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除资产")
    public Result<Void> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return Result.success(null);
    }
}