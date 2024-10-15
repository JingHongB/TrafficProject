package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.BaseLocationEntity;
import com.example.model.entity.FurnitureFactory;
import com.example.model.entity.Lumberyard;

import java.util.List;

public interface FurnitureFactoryService extends IService<FurnitureFactory> {
    /**
     * 获取所有家具厂信息。
     *
     * @return 家具厂列表
     */
    List<BaseLocationEntity> getAllFurnitureFactories();

    /**
     * 根据名称获取家具厂信息。
     *
     * @param name 家具场名称
     * @return 家具场信息
     */
    FurnitureFactory getFurnitureFactoryByName(String name);
}
