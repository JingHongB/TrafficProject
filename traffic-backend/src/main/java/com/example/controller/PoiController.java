package com.example.controller;

import com.example.model.RestBean;
import com.example.model.dto.SearchRequestDTO;
import com.example.model.BaseLocationEntity;
import com.example.service.FurnitureFactoryService;
import com.example.service.LumberyardService;
import com.example.service.PoiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/poi")
@Tag(name = "Poi", description = "Poi相关操作")
public class PoiController {
    @Resource
    private PoiService poiService;
    @Resource
    private LumberyardService lumberyardService;
    @Resource
    private FurnitureFactoryService furnitureFactoryService;

    /**
     * 处理搜索请求，根据前端传递的城市、关键词和页数，通过 poiService 执行搜索。
     *
     * @param request 包含城市、关键词和页数的请求体
     * @return 搜索结果封装在 RestBean 中
     */
    @Operation(summary = "搜索POI")
    @PostMapping("/search")
    public RestBean<?> search(@RequestBody SearchRequestDTO request) {
        try {
            return RestBean.success(poiService.searchPoi(request.getCity(), request.getKeyword(), request.getPageNum()));
        } catch (Exception e) {
            return RestBean.failure(500, "搜索失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有 POI 数据
     *
     * @return 所有 POI 数据封装在 RestBean 中
     */
    @Operation(summary = "获取所有POI数据")
    @GetMapping("/get")
    public RestBean<Map<String, List<BaseLocationEntity>>> getAllLumberyards() {
        try {
            List<BaseLocationEntity> lumberyards = lumberyardService.getAllLumberyards();
            List<BaseLocationEntity> furnitureFactories = furnitureFactoryService.getAllFurnitureFactories();
            // 创建结果 Map，将数据存储到 Map 中
            Map<String, List<BaseLocationEntity>> result = new HashMap<>();
            result.put("lumberyard", lumberyards);
            result.put("furnitureFactory", furnitureFactories);
            return RestBean.success(result);
        } catch (Exception e) {
            return RestBean.failure(500, "获取POI数据失败: " + e.getMessage());
        }
    }
}
