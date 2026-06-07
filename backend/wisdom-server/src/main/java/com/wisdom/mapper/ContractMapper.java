package com.wisdom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisdom.entity.Contract;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ContractMapper extends BaseMapper<Contract> {
    @Select("select * from bus_contract where house_id = #{houseId}")
    List<Contract> selectByHouseId(Long houseId);
}