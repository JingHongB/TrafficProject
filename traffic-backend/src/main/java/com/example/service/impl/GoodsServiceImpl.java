package com.example.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.GoodsMapper;
import com.example.model.entity.Goods;
import com.example.model.entity.GoodsType;
import com.example.model.entity.PoiType;
import com.example.model.vo.GoodsVO;
import com.example.service.GoodsService;
import com.example.service.GoodsTypeService;
import com.example.service.PoiService;
import com.example.service.PoiTypeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
    @Resource
    private PoiService poiService;
    @Resource
    private PoiTypeService poiTypeService;
    @Resource
    private GoodsTypeService goodsTypeService;

    private final Random random = new Random();

    /**
     * 创建货物
     */
    @Override
    public void createGoods() {
        //只在缺货的一级工厂生成货物
        poiService.query().eq("status", "缺货").list().forEach(poi -> {
            PoiType poiType = poiTypeService.getById(poi.getTypeId());
            if (poiType != null && poiType.getFatherNode() == null) {
                Goods goods = new Goods();
                goods.setId(IdUtil.getSnowflakeNextId());
                goods.setTypeId(poiType.getGoodsTypeId());
                goods.setOwnerId(poi.getId());
                GoodsType goodsType = goodsTypeService.getById(goods.getTypeId());
                //随机生成重量
                double weight = goodsType.getMinWeight() +
                        (goodsType.getMaxWeight() - goodsType.getMinWeight()) * random.nextDouble();
                weight = Math.round(weight * 10.0) / 10.0;
                goods.setWeight(weight);
                save(goods);
                poiService.update().set("status", "有货").eq("id", poi.getId()).update();
            }
        });
        log.info("成功生成货物");
    }

    /**
     * Goods列表转换为GoodsVO列表
     *
     * @param goodsList Goods列表
     * @return GoodsVO列表
     */
    @Override
    public List<GoodsVO> convertToVO(List<Goods> goodsList) {
        List<GoodsVO> goodsVOList = new ArrayList<>();
        goodsList.forEach(goods -> {
            GoodsVO goodsVO = new GoodsVO();
            BeanUtils.copyProperties(goods, goodsVO);
            goodsVO.setType(goodsTypeService.getById(goods.getTypeId()).getName());
            goodsVO.setOwner(poiService.getById(goods.getOwnerId()).getName());
            goodsVOList.add(goodsVO);
        });
        return goodsVOList;
    }
}
