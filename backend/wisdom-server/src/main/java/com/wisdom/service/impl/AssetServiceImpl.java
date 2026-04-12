package com.wisdom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.dto.AssetDTO;
import com.wisdom.dto.AssetPageQueryDTO;
import com.wisdom.entity.House;
import com.wisdom.mapper.AssetMapper;
import com.wisdom.result.PageResult;
import com.wisdom.service.AssetService;
import com.wisdom.vo.AssetTreeVO;
import com.wisdom.vo.AssetVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AssetServiceImpl implements AssetService {

    @Autowired
    private AssetMapper assetMapper;

    @Override
    public List<AssetTreeVO> getAssetTree() {
        List<House> houses = assetMapper.selectAllAssets();
        Map<Long, List<House>> parentMap = houses.stream()
                .collect(Collectors.groupingBy(House::getParentId));
        return buildAssetTree(0L, parentMap);
    }

    private List<AssetTreeVO> buildAssetTree(Long parentId, Map<Long, List<House>> parentMap) {
        List<AssetTreeVO> treeVOs = new ArrayList<>();
        List<House> children = parentMap.get(parentId);
        if (children != null) {
            for (House house : children) {
                AssetTreeVO treeVO = new AssetTreeVO();
                treeVO.setId(house.getId());
                treeVO.setName(house.getName());
                treeVO.setType(house.getType());
                treeVO.setChildren(buildAssetTree(house.getId(), parentMap));
                treeVOs.add(treeVO);
            }
        }
        return treeVOs;
    }

    @Override
    public PageResult<AssetVO> getAssetList(AssetPageQueryDTO assetPageQueryDTO) {
        Page<House> page = new Page<>(assetPageQueryDTO.getPage(), assetPageQueryDTO.getPageSize());
        LambdaQueryWrapper<House> queryWrapper = new LambdaQueryWrapper<>();
        if (assetPageQueryDTO.getName() != null) {
            queryWrapper.like(House::getName, assetPageQueryDTO.getName());
        }
        if (assetPageQueryDTO.getType() != null) {
            queryWrapper.eq(House::getType, assetPageQueryDTO.getType());
        }
        if (assetPageQueryDTO.getStatus() != null) {
            queryWrapper.eq(House::getStatus, assetPageQueryDTO.getStatus());
        }
        IPage<House> assetPage = assetMapper.selectPage(page, queryWrapper);
        List<AssetVO> assetVOList = assetPage.getRecords().stream()
                .map(house -> {
                    AssetVO assetVO = new AssetVO();
                    BeanUtils.copyProperties(house, assetVO);
                    return assetVO;
                })
                .collect(Collectors.toList());
        return new PageResult<>(assetPage.getTotal(), assetVOList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateAsset(AssetDTO assetDTO) {
        House house = new House();
        BeanUtils.copyProperties(assetDTO, house);
        
        if (house.getOwnerName() != null && house.getOwnerName().trim().isEmpty()) {
            house.setOwnerName(null);
        }
        if (house.getOwnerPhone() != null && house.getOwnerPhone().trim().isEmpty()) {
            house.setOwnerPhone(null);
        }
        
        if (assetDTO.getId() == null) {
            assetMapper.insert(house);
        } else {
            House existHouse = assetMapper.selectById(assetDTO.getId());
            if (existHouse == null) {
                assetMapper.insert(house);
            } else {
                assetMapper.updateById(house);
            }
        }
    }

    @Override
    public void deleteAsset(Long id) {
        assetMapper.deleteById(id);
    }
}