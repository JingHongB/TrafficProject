package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Goods;

import java.util.List;

public interface GoodsService extends IService<Goods> {
    /**
     * 生成货物
     */
    void createGoods();

    /**
     * 获取未分配货物
     *
     * @return 未分配货物列表
     */
    List<Goods> getUnassignedGoods();

    /**
     * 获取所有货物
     *
     * @return 所有货物列表
     */
    List<Goods> getAllGoods();
}
