package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.GoodsTypeMapper;
import com.example.model.entity.GoodsType;
import com.example.service.GoodsTypeService;
import org.springframework.stereotype.Service;

@Service
public class GoodsTypeServiceImpl extends ServiceImpl<GoodsTypeMapper, GoodsType> implements GoodsTypeService {
}
