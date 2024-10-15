package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Vehicle;

import java.util.List;

public interface VehicleService extends IService<Vehicle> {


    /**
     * 获取所有车辆信息
     *
     * @return 车辆信息列表
     */
    List<Vehicle> getAllVehicles();

    /**
     * 初始化车辆信息
     */
    void initializeVehicles();

    /**
     * 获取未分配任务的车辆
     *
     * @return 未分配任务的车辆列表
     */
    List<Vehicle> getUnassignedVehicles();


}
