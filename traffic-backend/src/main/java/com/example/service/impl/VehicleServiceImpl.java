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

    @Override
    public List<Vehicle> getAllVehicles() {
        return list();
    }

    @Override
    public void initializeVehicles() {
        //截断表
        vehicleMapper.truncate();

        List<Vehicle> vehicles = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            Vehicle vehicle = new Vehicle();
            vehicle.setStatus("空闲");
            vehicle.setLongitude(String.valueOf(97.35 + (108.54 - 97.35) * random.nextDouble()));  // 随机生成经度
            vehicle.setLatitude(String.valueOf(26.05 + (34.31 - 26.05) * random.nextDouble()));    // 随机生成纬度
            vehicles.add(vehicle);
        }
        vehicles.forEach(vehicleMapper::insert);
    }
}
