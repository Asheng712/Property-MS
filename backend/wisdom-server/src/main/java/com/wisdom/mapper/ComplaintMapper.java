package com.wisdom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisdom.entity.Complaint;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface ComplaintMapper extends BaseMapper<Complaint> {

    @Update("UPDATE bus_complaint SET source = #{source} WHERE reporter_id = #{reporterId}")
    int updateSourceByReporterId(@Param("reporterId") Long reporterId, @Param("source") String source);
}