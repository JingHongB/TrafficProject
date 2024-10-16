package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.PoiTypeMapper;
import com.example.model.entity.PoiType;
import com.example.service.PoiTypeService;
import org.springframework.stereotype.Service;

@Service
public class PoiTypeServiceImpl extends ServiceImpl<PoiTypeMapper, PoiType> implements PoiTypeService {
}
