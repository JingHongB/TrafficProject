package com.example.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.TaskMapper;
import com.example.model.entity.*;
import com.example.model.vo.TaskVO;
import com.example.service.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
    @Resource
    private GoodsService goodsService;
    @Resource
    private GoodsTypeService goodsTypeService;
    @Resource
    private PoiService poiService;
    @Resource
    private PoiTypeService poiTypeService;
    @Resource
    private CarService carService;

    private final Random random = new Random();


    /**
     * 创建委托
     */
    @Override
    public void createTask() {
        List<Goods> unassignedGoods = goodsService.query().eq("status", "待委托").list();
        if (unassignedGoods.isEmpty()) {
            log.info("没有需要委托的货物");
            return;
        }
        for (Goods goods : unassignedGoods) {
            Task task = new Task();
            task.setId(IdUtil.getSnowflakeNextId());
            task.setGoodsId(goods.getId());
            task.setStartId(goods.getOwnerId());
            //随机选择下游工厂作为终点
            PoiType poiType = poiTypeService.getById(poiService.getById(goods.getOwnerId()).getTypeId());
            List<Poi> endPoi = poiService.query().eq("type_id", poiType.getChildNode()).list();
            task.setEndId(endPoi.get(random.nextInt(endPoi.size())).getId());
            //根据起点和终点计算距离
            task.setDistance(calculateDistance(poiService.getById(task.getStartId()), poiService.getById(task.getEndId())));
            this.save(task);
            goods.setStatus("已委托");
            goodsService.updateById(goods);
        }
        log.info("成功根据货物创建委托");
    }

    /**
     * 分配委托
     */
    @Override
    public void assignTask() {
        List<Task> tasks = this.query().eq("status", "待接取").list();
        List<Car> cars = carService.query().eq("status", "空闲").list();

        // 为每个委托寻找最近的空闲车辆
        for (Task task : tasks) {
            Car nearestCar = null;
            double minDistance = Double.MAX_VALUE;

            for (Car car : cars) {
                double distance = calculateDistance(car, poiService.getById(task.getStartId()));
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestCar = car;
                }
            }
            // 分配委托
            if (nearestCar != null) {
                task.setCarId(nearestCar.getId());
                task.setStatus("已分配");
                nearestCar.setStatus("运输中");

                // 更新委托和车辆信息
                this.updateById(task);
                carService.updateById(nearestCar);

                // 将车辆从空闲车辆列表中移除，防止重复分配
                cars.remove(nearestCar);
            }
        }
    }

    /**
     * 将Task对象列表转换为TaskVO对象列表
     *
     * @param tasks Task对象列表
     * @return TaskVO对象列表
     */
    @Override
    public List<TaskVO> convertToTaskVO(List<Task> tasks) {
        List<TaskVO> taskVOList = new ArrayList<>();
        for (Task task : tasks) {
            TaskVO taskVO = convertToTaskVO(task);
            taskVOList.add(taskVO);
        }
        return taskVOList;
    }

    /**
     * 将Task对象转换为TaskVO对象
     *
     * @param task Task对象
     * @return TaskVO对象
     */
    private TaskVO convertToTaskVO(Task task) {
        TaskVO taskVO = new TaskVO();
        BeanUtils.copyProperties(task, taskVO);
        taskVO.setId(String.valueOf(task.getId()));
        taskVO.setCarId(String.valueOf(task.getCarId()));
        taskVO.setGoods(goodsTypeService.getById(goodsService.getById(task.getGoodsId()).getTypeId()).getName());
        taskVO.setStartPoint(poiService.getById(task.getStartId()).getName());
        taskVO.setEndPoint(poiService.getById(task.getEndId()).getName());
        taskVO.setStartLongitude(poiService.getById(task.getStartId()).getLongitude());
        taskVO.setStartLatitude(poiService.getById(task.getStartId()).getLatitude());
        taskVO.setEndLongitude(poiService.getById(task.getEndId()).getLongitude());
        taskVO.setEndLatitude(poiService.getById(task.getEndId()).getLatitude());
        Car car = carService.getById(task.getCarId());
        if (car != null) {
            taskVO.setCarLongitude(car.getLongitude());
            taskVO.setCarLatitude(car.getLatitude());
        }
        return taskVO;
    }

    /**
     * 计算两个Poi点之间的距离（经纬度）
     *
     * @param startPoi 起点POI对象
     * @param endPoi   终点POI对象
     * @return 距离（单位：米）
     */
    private double calculateDistance(Poi startPoi, Poi endPoi) {
        double R = 6371; // 地球半径，单位为千米

        double lon1 = startPoi.getLongitude();
        double lat1 = startPoi.getLatitude();
        double lon2 = endPoi.getLongitude();
        double lat2 = endPoi.getLatitude();

        double latRad1 = Math.toRadians(lat1);
        double latRad2 = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(latRad1) * Math.cos(latRad2) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c; // 计算距离（单位：千米）
        return Math.round(distance * 10.0) / 10.0; // 返回精确到一位小数的距离
    }

    /**
     * 计算车辆到POI点的距离
     *
     * @param car 车辆
     * @param poi Poi
     * @return 距离（单位：千米）
     */
    private double calculateDistance(Car car, Poi poi) {
        double R = 6371; // 地球半径，单位为千米

        double lon1 = car.getLongitude();
        double lat1 = car.getLatitude();
        double lon2 = poi.getLongitude();
        double lat2 = poi.getLatitude();

        double latRad1 = Math.toRadians(lat1);
        double latRad2 = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(latRad1) * Math.cos(latRad2) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c; // 计算距离（单位：千米）
        return Math.round(distance * 10.0) / 10.0; // 返回精确到一位小数的距离
    }
}
