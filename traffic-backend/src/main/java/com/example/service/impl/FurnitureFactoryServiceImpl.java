package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.FurnitureFactoryMapper;
import com.example.model.BaseLocationEntity;
import com.example.model.entity.FurnitureFactory;
import com.example.model.entity.Lumberyard;
import com.example.service.FurnitureFactoryService;
import com.example.utils.PoiUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FurnitureFactoryServiceImpl extends ServiceImpl<FurnitureFactoryMapper, FurnitureFactory> implements FurnitureFactoryService {
    @Resource
    private FurnitureFactoryMapper furnitureFactoryMapper;

    /**
     * 获取所有家具厂信息，并转换为 BaseLocationEntity 类型。
     *
     * @return 家具厂列表
     */
    @Override
    public List<BaseLocationEntity> getAllFurnitureFactories() {
        List<FurnitureFactory> furnitureFactories = list();
        return PoiUtils.PoiConvert(furnitureFactories, BaseLocationEntity.class);
    }

    @Override
    public FurnitureFactory getFurnitureFactoryByName(String name) {
        QueryWrapper<FurnitureFactory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        return furnitureFactoryMapper.selectOne(queryWrapper);
    }
}
