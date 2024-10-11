package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.BaseLocationEntity;
import com.example.model.entity.FurnitureFactory;

import java.util.List;

public interface FurnitureFactoryService extends IService<FurnitureFactory> {
    /**
     * 获取所有家具厂信息。
     *
     * @return 家具厂列表
     */
    List<BaseLocationEntity> getAllFurnitureFactories();
}
