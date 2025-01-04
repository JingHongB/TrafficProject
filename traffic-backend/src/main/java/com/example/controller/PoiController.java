package com.example.controller;

import com.example.model.RestBean;
import com.example.model.dto.PoiDTO;
import com.example.model.entity.Poi;
import com.example.model.vo.PoiSearchVO;
import com.example.model.vo.PoiVO;
import com.example.service.PoiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/poi")
@Tag(name = "Poi", description = "Poi相关操作")
@Slf4j
public class PoiController {
    @Resource
    private PoiService poiService;

    /**
     * 根据城市、关键词和页数，通过高德API进行搜索。
     *
     * @param city    搜索的城市
     * @param keyword 搜索的关键词
     * @param pageNum 页数
     * @return 搜索结果
     */
    @Operation(summary = "搜索Poi")
    @GetMapping("/search")
    public RestBean<List<PoiSearchVO>> search(
            @RequestParam String city,
            @RequestParam String keyword,
            @RequestParam int pageNum) {
        try {
            List<Poi> pois = poiService.searchPoi(city, keyword, pageNum);
            return RestBean.success(poiService.PoiConvertToPoiSearchVO(pois));
        } catch (Exception e) {
            log.error("搜索Poi失败", e);
            return RestBean.failure(500, "搜索失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有 POI 数据
     *
     * @return 所有 POI 数据封装在 RestBean 中
     */
    @Operation(summary = "获取Poi")
    @GetMapping("/list")
    public RestBean<List<PoiVO>> getAllLumberyards() {
        try {
            List<Poi> pois = poiService.list();
            return RestBean.success(poiService.PoiConvertToPoiVO(pois));
        } catch (Exception e) {
            log.error("获取POI数据失败", e);
            return RestBean.failure(500, "获取POI数据失败: " + e.getMessage());
        }
    }

    /**
     * 删除指定 ID 的 POI 数据
     * zjm
     *
     * @param id 要删除的 POI 的 ID
     */
    @Operation(summary = "删除Poi")
    @PostMapping("/delete/{id}")
    public RestBean<Void> deletePoi(@PathVariable("id") long id) {
        try {
            poiService.removeById(id);
            return RestBean.success();
        } catch (Exception e) {
            log.error("删除POI数据失败", e);
            return RestBean.failure(500, "删除POI数据失败: " + e.getMessage());
        }
    }

    /**
     * 新增 POI 数据
     * zjm
     *
     * @param poiDTO 要新增的 POI 数据
     */
    @Operation(summary = "新增Poi")
    @PostMapping("/add")
    public RestBean<Void> addPoi(@RequestBody PoiDTO poiDTO) {
        try {
            poiService.addPoi(poiDTO);
            return RestBean.success();
        } catch (Exception e) {
            log.error("新增POI数据失败", e);
            return RestBean.failure(500, "新增POI数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取指定 ID 的 POI 数据
     * zjm
     *
     * @param id 要获取的 POI 的 ID
     */
    @Operation(summary = "根据id查询Poi")
    @GetMapping("/get/{id}")
    public RestBean<PoiVO> getPoiById(@PathVariable("id") long id) {
        try {
            Poi poi = poiService.getById(id);
            return RestBean.success(poiService.PoiConvertToPoiVO(List.of(poi)).get(0));
        } catch (Exception e) {
            log.error("获取POI数据失败", e);
            return RestBean.failure(500, "获取POI数据失败: " + e.getMessage());
        }
    }

    /**
     * 更新指定 ID 的 POI 数据
     * zjm
     *
     * @param poiDTO 要更新的 POI 数据
     */
    @Operation(summary = "更新Poi")
    @PutMapping("/update")
    public RestBean<Void> updatePoi(@RequestBody PoiDTO poiDTO) {
        try {
            poiService.updatePoi(poiDTO);
            return RestBean.success();
        } catch (Exception e) {
            log.error("更新POI数据失败", e);
            return RestBean.failure(500, "更新POI数据失败: " + e.getMessage());
        }
    }
}
