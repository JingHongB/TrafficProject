package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.BaseLocationEntity;
import com.example.model.entity.Lumberyard;

import java.util.List;

public interface LumberyardService extends IService<Lumberyard> {
    /**
     * 获取所有伐木场信息。
     *
     * @return 伐木场列表
     */
    List<BaseLocationEntity> getAllLumberyards();
}
