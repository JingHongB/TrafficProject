package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Goods;

import java.util.List;

public interface GoodsService extends IService<Goods> {
    /**
     * 生成货物
     */
    void createGoods();
}
