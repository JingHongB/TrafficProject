package com.example.controller;

import com.example.model.RestBean;
import com.example.model.dto.GoodsDTO;
import com.example.model.entity.Goods;
import com.example.model.vo.GoodsVO;
import com.example.service.GoodsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goods")
@Tag(name = "货物", description = "货物相关操作")
@Slf4j
public class GoodsController {
    @Resource
    private GoodsService goodsService;

    /**
     * 创建货物
     *zjm
     * @return Void
     */
    @Operation(summary = "创建货物")
    @PostMapping("/create")
    public RestBean<Void> createGoods() {
        try {
            goodsService.createGoods();
            return RestBean.success();
        } catch (Exception e) {
            log.error("创建货物失败", e);
            return RestBean.failure(500, "创建货物失败");
        }
    }

    /**
     * 查询所有货物信息
     *zjm
     * @return 所有货物列表
     */
    @Operation(summary = "获取货物")
    @GetMapping("/list")
    public RestBean<List<GoodsVO>> getGoodsList() {
        try {
            List<Goods> goodsList = goodsService.list();
            return RestBean.success(goodsService.convertToVO(goodsList));
        } catch (Exception e) {
            log.error("获取货物失败", e);
            return RestBean.failure(500, "获取货物失败");
        }
    }
    /**
     * 删除货物
     *zjm
     * @param id 货物id
     * @return Void
     */
    @Operation(summary = "删除货物")
    @PostMapping("/delete/{id}")
    public RestBean<Void> deleteGoods(@PathVariable("id") long id) {
        try {
            goodsService.removeById(id);
            return RestBean.success();
        } catch (Exception e) {
            log.error("删除货物失败", e);
            return RestBean.failure(500, "删除货物失败");
        }
    }

    /**
     * 增加一件货物
     * zjm
     * @param goodsDTO 货物信息
     * @return Void
     */
    @Operation(summary = "新增一件货物")
    @PostMapping("/add")
    public RestBean<Void> addGoods(@RequestBody GoodsDTO goodsDTO) {
        try {
            goodsService.addGood(goodsDTO);
            return RestBean.success();
        } catch (Exception e) {
            log.error("新增货物失败", e);
            return RestBean.failure(500, "新增货物失败");
        }
    }
    /**
     * 根据id查询货物信息
     * zjm
     * @param id 货物id
     *
     */
    @Operation(summary = "根据id查询货物信息")
    @GetMapping("/get/{id}")
    public RestBean<GoodsVO> getGoodsById(@PathVariable("id") long id) {
        try {
            Goods goods = goodsService.getById(id);
            return RestBean.success(goodsService.convertToOneVO(goods));
        } catch (Exception e) {
            log.error("获取货物信息失败", e);
            return RestBean.failure(500, "获取货物信息失败");
       }
    }

    /**
     * 更新一件货物
     * zjm
     * @param goodsDTO 货物信息
     * @return Void
     */
    @Operation(summary = "更新一件货物")
    @PutMapping("/update")
    public RestBean<Void> updateGoods(@RequestBody GoodsDTO goodsDTO) {
        try {
            goodsService.updateGood(goodsDTO);
            return RestBean.success();
        } catch (Exception e) {
            log.error("更新货物失败", e);
            return RestBean.failure(500, "更新货物失败");
        }
    }
}
