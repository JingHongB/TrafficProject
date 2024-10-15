package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.BaseLocationEntity;
import com.example.model.entity.Lumberyard;

import java.util.List;

public interface LumberyardService extends IService<Lumberyard> {
    /**
     * 获取所有伐木场信息。
     *
     * @return 伐木场列表
     */
    List<BaseLocationEntity> getAllLumberyards();

    /**
     * 根据名称获取伐木场信息。
     *
     * @param name 伐木场名称
     * @return 伐木场信息
     */
    //TODO 从伐木场延申到所有类型的地点
    Lumberyard getLumberyardByName(String name);
}
