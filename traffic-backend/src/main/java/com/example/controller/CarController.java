package com.example.controller;

import com.example.model.RestBean;
import com.example.model.dto.AddCarDTO;
import com.example.model.dto.EditCarDTO;
import com.example.model.vo.CarVO;
import com.example.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
     * @return Void
     */
    @Operation(summary = "初始化车辆")
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
    @Operation(summary = "获取车辆")
    @GetMapping("/list")
    public RestBean<List<CarVO>> getAllVehicles() {
        try {
            return RestBean.success(carService.getCarVOList());
        } catch (Exception e) {
            log.error("获取车辆信息失败", e);
            return RestBean.failure(500, "获取车辆信息失败" + e.getMessage());
        }
    }

    /**
     * 删除某一车辆信息
     * zjm
     *
     * @param id 车辆id
     * @return Void
     */
    @Operation(summary = "删除车辆")
    @PostMapping("/delete/{id}")
    public RestBean<Void> deleteCar(@PathVariable("id") long id) {
        try {
            carService.deleteCar(id);
            return RestBean.success();
        } catch (Exception e) {
            log.error("删除该车辆信息失败", e);
            return RestBean.failure(500, "删除车辆信息失败" + e.getMessage());
        }
    }

    /**
     * zjm
     * 新增某一车辆信息
     */
    @Operation(summary = "新增一辆车")
    @PostMapping("/add")
    public RestBean<Void> addCar(@RequestBody AddCarDTO addCarDTO) {
        try {
            carService.addCar(addCarDTO);
            return RestBean.success();
        } catch (Exception e) {
            log.error("新增一辆车失败", e);
            return RestBean.failure(500, "新增一辆车失败" + e.getMessage());
        }
    }

    /**
     * zjm
     *
     * @param id 根据id查询车辆信息
     */
    @Operation(summary = "根据id查询车辆信息")
    @GetMapping("/get/{id}")
    public RestBean<CarVO> getCarById(@PathVariable("id") long id) {
        try {
            return RestBean.success(carService.getCarById(id));
        } catch (Exception e) {
            log.error("根据id查询车辆信息失败", e);
            return RestBean.failure(500, "根据id查询车辆信息失败" + e.getMessage());
        }
    }

    /**
     * 修改车辆信息
     * zjm
     *
     * @param editCarDTO 车辆信息数据传输对象
     * @return 操作结果
     */
    @Operation(summary = "更新车辆信息")
    @PostMapping("/update")
    public RestBean<Void> updateCar(@RequestBody EditCarDTO editCarDTO) {
        try {
            carService.updateCar(editCarDTO);
            return RestBean.success();
        } catch (Exception e) {
            log.error("更新车辆信息失败", e);
            return RestBean.failure(500, "更新车辆信息失败" + e.getMessage());
        }
    }

}
