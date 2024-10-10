package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.FurnitureFactoryMapper;
import com.example.model.entity.BaseLocationEntity;
import com.example.model.entity.FurnitureFactory;
import com.example.model.entity.Lumberyard;
import com.example.service.FurnitureFactoryService;
import com.example.utils.PoiUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FurnitureFactoryServiceImpl extends ServiceImpl<FurnitureFactoryMapper, FurnitureFactory> implements FurnitureFactoryService {
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
}
