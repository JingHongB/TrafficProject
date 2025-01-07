package com.example.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.GoodsMapper;
import com.example.model.dto.GoodsDTO;
import com.example.model.entity.Goods;
import com.example.model.entity.GoodsType;
import com.example.model.entity.Poi;
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
import java.util.Collections;
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
        // 查询所有状态为"有货"的工厂
        List<Poi> poiList = poiService.query().eq("status", "有货").list();

        // 确保至少有两个"有货"的工厂
        if (poiList.size() < 2) {
            log.warn("没有足够的工厂，无法生成货物。");
            return;  // 如果没有足够的工厂，直接返回
        }

        // 随机选择两个工厂
        Collections.shuffle(poiList);  // 打乱列表顺序
        Poi selectedPoi1 = poiList.get(0);  // 选择第一个工厂
        Poi selectedPoi2 = poiList.get(1);  // 选择第二个工厂

        // 创建货物并分别分配给这两个工厂
        createGoodsForPoi(selectedPoi1);
        createGoodsForPoi(selectedPoi2);

        log.info("成功生成货物");
        log.info("成功生成货物");
    }

    /**
     * 根据指定工厂创建货物
     *
     * @param poi 工厂信息
     */
    private void createGoodsForPoi(Poi poi) {
        PoiType poiType = poiTypeService.getById(poi.getTypeId());
        Goods goods = new Goods();
        // 生成货物ID
        goods.setId(IdUtil.getSnowflakeNextId());
        // 生成货物类型ID
        goods.setTypeId(poiType.getGoodsTypeId());
        // 生成货物所属工厂
        goods.setOwnerId(poi.getId());
        // 随机生成重量
        GoodsType goodsType = goodsTypeService.getById(goods.getTypeId());
        double weight = goodsType.getMinWeight() +
                (goodsType.getMaxWeight() - goodsType.getMinWeight()) * random.nextDouble();
        weight = Math.round(weight * 10.0) / 10.0;
        goods.setWeight(weight);
        save(goods);
        // 更新工厂状态(有货->缺货)
        poiService.update().set("status", "缺货").eq("id", poi.getId()).update();

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
     *
     * @param goods 货物信息
     *              zjm
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
     *
     * @param goodsDTO 货物信息
     *                 zjm
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
