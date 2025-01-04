package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.GoodsDTO;
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


    /**
     * 清空所有货物
     */
    void clearAllGoods();


    /**
     * 新增一件货物
     *
     * @param goodsDTO 货物信息
     */
    void addGood(GoodsDTO goodsDTO);

    /**
     * 将Goods转换为GoodsVO
     *
     * @param goods 货物
     * @return GoodsVO
     */
    GoodsVO convertToOneVO(Goods goods);

    /**
     * 更新一件货物
     *
     * @param goodsDTO 货物信息
     */
    void updateGood(GoodsDTO goodsDTO);
}
