package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.GoodsMapper;
import com.example.model.entity.FurnitureFactory;
import com.example.model.entity.Goods;
import com.example.model.entity.Lumberyard;
import com.example.service.FurnitureFactoryService;
import com.example.service.GoodsService;
import com.example.service.LumberyardService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    LumberyardService lumberyardService;

    @Resource
    FurnitureFactoryService furnitureFactoryService;

    private final Random random = new Random();

    /**
     * 创建货物
     */
    //TODO: 扩展成创建各种类型的货物
    @Override
    public void createGoods() {
        List<Lumberyard> lumberyards = lumberyardService.list();
        List<FurnitureFactory> furnitureFactories = furnitureFactoryService.list();
        for (Lumberyard lumberyard : lumberyards) {
            if (!lumberyard.isHasGoods()) {
                Goods goods = new Goods();
                goods.setType("木材");
                goods.setStartPoint(lumberyard.getName());
                goods.setEndPoint(furnitureFactories.get(random.nextInt(furnitureFactories.size())).getName());
                goods.setStatus("待装载");
                goodsMapper.insert(goods);
                //更新伐木场状态
                lumberyard.setHasGoods(true);
                lumberyardService.updateById(lumberyard);
            }
        }
    }
}
