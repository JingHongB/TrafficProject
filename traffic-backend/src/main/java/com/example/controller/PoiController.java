package com.example.controller;

import com.example.model.RestBean;
import com.example.model.dto.SearchRequestDTO;
import com.example.service.PoiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/poi")
@Tag(name = "Poi", description = "Poi相关操作")
public class PoiController {
    @Resource
    private PoiService poiService;

    /**
     * 处理搜索请求，根据前端传递的城市、关键词和页数，通过 poiService 执行搜索。
     *
     * @param request 包含城市、关键词和页数的请求体
     * @return 搜索结果封装在 RestBean 中
     */
    @Operation(summary = "根据城市、关键词和页数搜索信息")
    @PostMapping("/search")
    public RestBean<?> search(@RequestBody SearchRequestDTO request) {
        try {
            return RestBean.success(poiService.searchPoi(request.getCity(), request.getKeyword(), request.getPageNum()));
        } catch (Exception e) {
            return RestBean.failure(500, "搜索失败: " + e.getMessage());
        }
    }
}
