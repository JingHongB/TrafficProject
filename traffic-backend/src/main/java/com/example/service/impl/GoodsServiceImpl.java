package com.example.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.GoodsMapper;
import com.example.model.dto.GoodsDTO;
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
                //生成货物ID
                goods.setId(IdUtil.getSnowflakeNextId());
                //生成货物类型ID
                goods.setTypeId(poiType.getGoodsTypeId());
                //生成货物所属工厂
                goods.setOwnerId(poi.getId());
                //随机生成重量
                GoodsType goodsType = goodsTypeService.getById(goods.getTypeId());
                double weight = goodsType.getMinWeight() +
                        (goodsType.getMaxWeight() - goodsType.getMinWeight()) * random.nextDouble();
                weight = Math.round(weight * 10.0) / 10.0;
                goods.setWeight(weight);
                save(goods);
                //更新工厂状态(缺货 -> 有货)
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
            goodsVO.setId(String.valueOf(goods.getId()));
            goodsVO.setType(goodsTypeService.getById(goods.getTypeId()).getName());
            goodsVO.setOwner(poiService.getById(goods.getOwnerId()).getName());
            goodsVOList.add(goodsVO);
        });
        return goodsVOList;
    }
    /**
     * 将goods转换为GoodsVO
     * @param goods 货物信息
     * zjm
     * @return GoodsVO
     */
    public GoodsVO convertToOneVO(Goods goods) {
        GoodsVO goodsVO = new GoodsVO();
        BeanUtils.copyProperties(goods, goodsVO);
        goodsVO.setId(String.valueOf(goods.getId()));
        goodsVO.setType(goodsTypeService.getById(goods.getTypeId()).getName());
        goodsVO.setOwner(poiService.getById(goods.getOwnerId()).getName());
        return goodsVO;
    }

    /**
     * 更新货物信息
     * @param goodsDTO 货物信息
     * zjm
     */
    @Override
    public void updateGood(GoodsDTO goodsDTO) {
        Goods goods = new Goods();
        // 设置 ID，默认值为 0L
        goods.setId(goodsDTO.getId() != null ? Long.parseLong(goodsDTO.getId()) : 0L);
        // 设置 TypeId，默认值为 0L
        goods.setTypeId(goodsDTO.getTypeId() != null ? Long.parseLong(goodsDTO.getTypeId()) : 0L);
        // 设置 OwnerId，默认值为 0L
        goods.setOwnerId(goodsDTO.getOwnerId() != null ? Long.parseLong(goodsDTO.getOwnerId()) : 0L);
        // 设置 Weight，默认值为 0.0
        goods.setWeight(goodsDTO.getWeight() != null ? Double.parseDouble(String.valueOf(goodsDTO.getWeight())) : 0.0);
        // 设置 Status，默认值为一个合理的默认状态，这里假设为 "inactive"
        goods.setStatus(goodsDTO.getStatus() != null ? goodsDTO.getStatus() : "inactive");
        updateById(goods);
    }

    /**
     * 清空所有货物
     */
    @Override
    public void clearAllGoods() {
        this.remove(new QueryWrapper<>());
    }

    /**
     * 新增一件货物
     *
     * @param goodsDTO 货物信息
     */
    @Override
    public void addGood(GoodsDTO goodsDTO) {
        Goods goods = new Goods();
        goods.setId(Long.parseLong(goodsDTO.getId()));
        goods.setTypeId(Long.parseLong(goodsDTO.getTypeId()));
        goods.setOwnerId(Long.parseLong(goodsDTO.getOwnerId()));
        goods.setWeight(Double.parseDouble(String.valueOf(goodsDTO.getWeight())));
        goods.setStatus("待委托");
        save(goods);
    }
}
