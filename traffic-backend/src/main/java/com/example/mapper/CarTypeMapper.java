package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.CarType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CarTypeMapper extends BaseMapper<CarType> {
}
