package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Goods;
import com.example.model.vo.GoodsVO;

import java.util.List;

public interface GoodsService extends IService<Goods> {
    /**
     * 生成货物
     */
    void createGoods();

    /**
     * Goods列表转换为GoodsVO列表
     *
     * @param goodsList Goods列表
     * @return GoodsVO列表
     */
    List<GoodsVO> convertToVO(List<Goods> goodsList);
}
