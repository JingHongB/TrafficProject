package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.CarTypeMapper;
import com.example.model.entity.CarType;
import com.example.service.CarTypeService;
import org.springframework.stereotype.Service;

@Service
public class CarTypeServiceImpl extends ServiceImpl<CarTypeMapper, CarType> implements CarTypeService {
}
