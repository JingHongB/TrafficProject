package com.example.controller;

import com.example.model.RestBean;
import com.example.service.CarService;
import com.example.service.GoodsService;
import com.example.service.PoiService;
import com.example.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/data")
@Tag(name = "数据", description = "数据相关操作")
@Slf4j
public class DataController {
    @Resource
    CarService carService;
    @Resource
    TaskService taskService;
    @Resource
    GoodsService goodsService;
    @Resource
    PoiService poiService;

    @Operation(summary = "重置项目状态")
    @PostMapping("/reset")
    public RestBean<Void> resetData() {
        try {
            // 清空货物和委托数据
            goodsService.clearAllGoods();
            taskService.clearAllTasks();
            // 重新生成车辆
            carService.initializeCars();
            // 重置工厂状态为缺货
            poiService.resetAllFactoryStatus();
            log.info("数据重置成功");
            return RestBean.success();
        } catch (Exception e) {
            log.error("数据重置失败", e);
            return RestBean.failure(500, "数据重置失败：" + e.getMessage());
        }
    }
}
