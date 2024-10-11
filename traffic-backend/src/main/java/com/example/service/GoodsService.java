package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Goods;

public interface GoodsService extends IService<Goods> {
    /**
     * 生成货物
     */
    void createGoods();
}
