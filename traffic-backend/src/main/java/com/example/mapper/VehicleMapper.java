package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.Vehicle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface VehicleMapper extends BaseMapper<Vehicle> {
    @Update("TRUNCATE TABLE vehicle")
    void truncate();
}
