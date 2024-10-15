package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.entity.Poi;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PoiMapper extends BaseMapper<Poi> {
}
