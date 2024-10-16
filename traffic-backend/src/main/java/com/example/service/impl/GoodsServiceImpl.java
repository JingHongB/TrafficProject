package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.GoodsMapper;
import com.example.mapper.PoiTypeMapper;
import com.example.model.entity.Goods;
import com.example.model.entity.Poi;
import com.example.service.GoodsService;
import com.example.service.PoiService;
import com.example.service.PoiTypeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
    @Resource
    private GoodsService goodsService;
    @Resource
    private PoiService poiService;
    @Resource
    private PoiTypeService poiTypeService;

    private final Random random = new Random();

    /**
     * 创建货物
     */
    @Override
    public void createGoods() {
        List<Poi> pois = poiService.getLackOfGoodsFactories();
//        for (Poi poi : pois) {
//            if (poi.getStatus().equals("缺货")) {
//                Goods goods = new Goods();
//                goods.setType("木材");
//                goods.setStartPoint(poi.getName());
//                goods.setEndPoint(furnitureFactories.get(random.nextInt(furnitureFactories.size())).getName());
//                goods.setStatus("待委托");
//                goodsMapper.insert(goods);
//                //更新伐木场状态
//                poi.setHasGoods(true);
//                lumberyardService.updateById(poi);
//            }
//        }
    }

    /**
     * 获取未分配货物
     *
     * @return 未分配货物列表
     */
    @Override
    public List<Goods> getUnassignedGoods() {
        return lambdaQuery().eq(Goods::getStatus, "待委托").list();
    }

    /**
     * 获取所有货物
     *
     * @return 所有货物列表
     */
    @Override
    public List<Goods> getAllGoods() {
        return list();
    }
}
