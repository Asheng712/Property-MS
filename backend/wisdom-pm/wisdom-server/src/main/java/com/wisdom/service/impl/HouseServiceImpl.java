package com.wisdom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisdom.dto.AssetPageQueryDTO;
import com.wisdom.entity.House;
import com.wisdom.mapper.HouseMapper;
import com.wisdom.result.PageResult;
import com.wisdom.service.HouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HouseServiceImpl implements HouseService {

    @Autowired
    private HouseMapper houseMapper;

    @Override
    public PageResult pageQuery(AssetPageQueryDTO dto) {
        Page<House> page = new Page<>(dto.getPage(), dto.getPageSize());
        QueryWrapper<House> queryWrapper = new QueryWrapper<>();
        
        if (dto.getName() != null) {
            queryWrapper.like("name", dto.getName());
        }
        if (dto.getType() != null) {
            queryWrapper.eq("type", dto.getType());
        }
        if (dto.getStatus() != null) {
            queryWrapper.eq("status", dto.getStatus());
        }
        
        houseMapper.selectPage(page, queryWrapper);
        
        return new PageResult(page.getTotal(), page.getRecords());
    }

    @Override
    public void save(House house) {
        houseMapper.insert(house);
    }

}