package com.example.service;

import com.example.model.entity.BaseLocationEntity;
import com.example.model.entity.FurnitureFactory;

import java.util.List;

public interface FurnitureFactoryService {
    /**
     * 获取所有家具厂信息。
     *
     * @return 家具厂列表
     */
    List<BaseLocationEntity> getAllFurnitureFactories();
}
