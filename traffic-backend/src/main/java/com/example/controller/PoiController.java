package com.example.controller;

import com.example.model.RestBean;
import com.example.model.dto.SearchRequestDTO;
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
    @GetMapping("/get")
    public RestBean<List<PoiVO>> getAllLumberyards() {
        try {
            List<Poi> pois = poiService.list();
            return RestBean.success(poiService.PoiConvertToPoiVO(pois));
        } catch (Exception e) {
            log.error("获取POI数据失败", e);
            return RestBean.failure(500, "获取POI数据失败: " + e.getMessage());
        }
    }
}
