package com.wisdom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisdom.entity.House;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AssetMapper extends BaseMapper<House> {
    @Select("select * from bus_house")
    List<House> selectAllAssets();
}