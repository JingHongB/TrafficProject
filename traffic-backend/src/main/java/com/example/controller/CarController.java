package com.example.controller;

import com.example.model.RestBean;
import com.example.model.vo.CarVO;
import com.example.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/car")
@Tag(name = "车辆", description = "车辆相关操作")
@Slf4j
public class CarController {
    @Resource
    private CarService carService;

    /**
     * 初始化车辆信息
     *
     * @return void
     */
    @Operation(summary = "初始化车辆信息")
    @PostMapping("/init")
    public RestBean<Void> init() {
        try {
            carService.initializeCars();
            return RestBean.success();
        } catch (Exception e) {
            log.error("初始化车辆信息失败", e);
            return RestBean.failure(500, "初始化车辆信息失败" + e.getMessage());
        }

    }

    /**
     * 获取所有车辆信息
     *
     * @return 所有车辆信息
     */
    @Operation(summary = "获取所有车辆信息")
    @GetMapping
    public RestBean<List<CarVO>> getAllVehicles() {
        try {
            return RestBean.success(carService.getCarVOList());
        } catch (Exception e) {
            log.error("获取车辆信息失败", e);
            return RestBean.failure(500, "获取车辆信息失败" + e.getMessage());
        }
    }

}
