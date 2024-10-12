package com.example.controller;

import com.example.model.RestBean;
import com.example.model.entity.Vehicle;
import com.example.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
@Tag(name = "车辆", description = "车辆相关操作")
public class VehicleController {
    @Resource
    private VehicleService vehicleService;

    /**
     * 初始化车辆信息
     *
     * @return 所有车辆信息
     */
    @Operation(summary = "初始化车辆信息并返回")
    @PostMapping("/init")
    public RestBean<List<Vehicle>> init() {
        try {
            vehicleService.initializeVehicles();
            return RestBean.success(vehicleService.getAllVehicles());
        } catch (Exception e) {
            return RestBean.failure(500, "初始化车辆信息失败");
        }

    }

    /**
     * 获取所有车辆信息
     *
     * @return 所有车辆信息
     */
    @Operation(summary = "获取所有车辆信息")
    @GetMapping
    public RestBean<List<Vehicle>> getAllVehicles() {
        try {
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            return RestBean.success(vehicles);
        } catch (Exception e) {
            return RestBean.failure(500, "获取车辆信息失败");
        }
    }

}
