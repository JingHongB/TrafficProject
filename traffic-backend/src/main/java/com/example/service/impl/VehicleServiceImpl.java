package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.VehicleMapper;
import com.example.model.entity.Vehicle;
import com.example.service.VehicleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class VehicleServiceImpl extends ServiceImpl<VehicleMapper, Vehicle> implements VehicleService {
    @Resource
    private VehicleMapper vehicleMapper;

    /**
     * 获取所有车辆
     *
     * @return 车辆列表
     */
    @Override
    public List<Vehicle> getAllVehicles() {
        return list();
    }

    /**
     * 初始化车辆
     */
    //TODO: 扩展车辆类型、数量
    @Override
    public void initializeVehicles() {
        //清空车辆表
        vehicleMapper.truncate();

        List<Vehicle> vehicles = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 1; i++) {
            Vehicle vehicle = new Vehicle();
            vehicle.setStatus("空闲");
            //车辆大致生成在四川省境内
            vehicle.setLongitude(String.valueOf(97.35 + (108.54 - 97.35) * random.nextDouble()));  // 随机生成经度
            vehicle.setLatitude(String.valueOf(26.05 + (34.31 - 26.05) * random.nextDouble()));    // 随机生成纬度
            vehicles.add(vehicle);
        }
        //插入数据
        vehicles.forEach(vehicleMapper::insert);
    }

    @Override
    public List<Vehicle> getUnassignedVehicles() {
        return lambdaQuery().eq(Vehicle::getStatus, "空闲").list();
    }
}
