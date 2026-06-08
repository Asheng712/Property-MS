package com.wisdom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisdom.entity.PropertyFeeConfig;
import org.apache.ibatis.annotations.Select;

public interface PropertyFeeConfigMapper extends BaseMapper<PropertyFeeConfig> {

    @Select("SELECT * FROM bus_property_fee_config WHERE status = 1 AND effective_month <= #{month} ORDER BY effective_month DESC LIMIT 1")
    PropertyFeeConfig getEffectiveConfig(String month);
}
