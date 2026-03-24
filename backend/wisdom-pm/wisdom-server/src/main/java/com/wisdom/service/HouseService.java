package com.wisdom.service;

import com.wisdom.dto.AssetPageQueryDTO;
import com.wisdom.entity.House;
import com.wisdom.result.PageResult;

public interface HouseService {

    PageResult pageQuery(AssetPageQueryDTO dto);

    void save(House house);

}