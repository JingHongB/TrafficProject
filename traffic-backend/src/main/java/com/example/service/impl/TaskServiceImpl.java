package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.FurnitureFactoryMapper;
import com.example.mapper.TaskMapper;
import com.example.mapper.VehicleMapper;
import com.example.model.entity.Goods;
import com.example.model.entity.Task;
import com.example.model.entity.Vehicle;
import com.example.model.vo.TaskVO;
import com.example.service.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
    @Resource
    private GoodsService goodsService;
    @Resource
    private VehicleService vehicleService;
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private VehicleMapper vehicleMapper;
    @Resource
    private LumberyardService lumberyardService;
    @Resource
    private FurnitureFactoryService furnitureFactoryService;


    @Override
    public void createTask() {
        List<Goods> unassignedGoods = goodsService.getUnassignedGoods();
        if (unassignedGoods.isEmpty()) {
            log.info("没有需要委托的货物");
            return;
        }
        for (Goods goods : unassignedGoods) {
            Task task = new Task();
            task.setGoodsId(goods.getId());
            task.setStartPoint(goods.getStartPoint());
            task.setEndPoint(goods.getEndPoint());
            task.setStatus("待分配");
            taskMapper.insert(task);

            goods.setStatus("已委托");
            goodsService.updateById(goods);
        }
    }

    /**
     * 获取所有委托
     *
     * @return 委托列表
     */
    @Override
    public List<Task> getAllTasks() {
        return list();
    }

    /**
     * 获取没有分配的委托
     *
     * @return 委托列表
     */
    @Override
    public List<Task> getUnassignedTasks() {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "待分配");

        return taskMapper.selectList(queryWrapper);
    }

    /**
     * 分配委托
     */
    @Override
    public void assignTask() {
        List<Task> tasks = getUnassignedTasks();
        List<Vehicle> vehicles = vehicleService.getUnassignedVehicles();

        // 为每个委托寻找最近的空闲车辆
        for (Task task : tasks) {
            Vehicle nearestVehicle = null;
            double minDistance = Double.MAX_VALUE;

            for (Vehicle vehicle : vehicles) {
                double distance = calculateDistance(lumberyardService.getLumberyardByName(task.getStartPoint()).getLongitude(),
                        lumberyardService.getLumberyardByName(task.getStartPoint()).getLatitude(),
                        vehicle.getLongitude(),
                        vehicle.getLatitude());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestVehicle = vehicle;
                }
            }
            // 分配委托
            if (nearestVehicle != null) {
                task.setVehicleId(nearestVehicle.getId());
                task.setStatus("已分配");
                nearestVehicle.setStatus("运输中");

                // 更新委托和车辆信息
                taskMapper.updateById(task);
                vehicleMapper.updateById(nearestVehicle);

                // 将车辆从空闲车辆列表中移除，防止重复分配
                vehicles.remove(nearestVehicle);
            }
        }
    }

    @Override
    public TaskVO convertToTaskVO(Task task) {
        TaskVO taskVO = new TaskVO();
        BeanUtils.copyProperties(task, taskVO);

        taskVO.setStartLongitude(lumberyardService.getLumberyardByName(task.getStartPoint()).getLongitude());
        taskVO.setStartLatitude(lumberyardService.getLumberyardByName(task.getStartPoint()).getLatitude());
        taskVO.setEndLongitude(furnitureFactoryService.getFurnitureFactoryByName(task.getEndPoint()).getLongitude());
        taskVO.setEndLatitude(furnitureFactoryService.getFurnitureFactoryByName(task.getEndPoint()).getLatitude());
        taskVO.setVehicleLongitude(vehicleService.getById(task.getVehicleId()).getLongitude());
        taskVO.setVehicleLatitude(vehicleService.getById(task.getVehicleId()).getLatitude());

        return taskVO;
    }

    /**
     * 计算两点之间的距离（经纬度）
     *
     * @param lon1 起点经度
     * @param lat1 起点纬度
     * @param lon2 终点经度
     * @param lat2 终点纬度
     * @return 距离（单位：米）
     */
    private double calculateDistance(String lon1, String lat1, String lon2, String lat2) {
        double R = 6371e3; // 地球半径，单位为米
        double latRad1 = Math.toRadians(Double.parseDouble(lat1));
        double latRad2 = Math.toRadians(Double.parseDouble(lat2));
        double deltaLat = Math.toRadians(Double.parseDouble(lat2) - Double.parseDouble(lat1));
        double deltaLon = Math.toRadians(Double.parseDouble(lon2) - Double.parseDouble(lon1));

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(latRad1) * Math.cos(latRad2) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // 返回距离（单位：米）
    }
}
