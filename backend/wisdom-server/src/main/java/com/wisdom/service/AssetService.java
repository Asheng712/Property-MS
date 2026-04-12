package com.wisdom.service;

import com.wisdom.dto.AssetDTO;
import com.wisdom.dto.AssetPageQueryDTO;
import com.wisdom.result.PageResult;
import com.wisdom.vo.AssetTreeVO;
import com.wisdom.vo.AssetVO;

import java.util.List;

public interface AssetService {
    List<AssetTreeVO> getAssetTree();
    PageResult<AssetVO> getAssetList(AssetPageQueryDTO assetPageQueryDTO);
    void saveOrUpdateAsset(AssetDTO assetDTO);
    void deleteAsset(Long id);
}