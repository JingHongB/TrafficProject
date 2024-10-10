package com.example.service;

import com.example.model.entity.Lumberyard;

import java.util.List;

public interface LumberyardService {
    /**
     * 获取所有伐木场信息。
     *
     * @return 伐木场列表
     */
    List<Lumberyard> getAllLumberyards();
}
