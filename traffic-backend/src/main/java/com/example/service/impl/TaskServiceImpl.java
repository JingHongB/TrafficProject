package com.example.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.TaskMapper;
import com.example.model.dto.TaskDTO;
import com.example.model.entity.*;
import com.example.model.vo.TaskVO;
import com.example.service.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private CarTypeService carTypeService;


    /**
     * 创建委托
     */
    @Override
    public void createTask() {
        List<Goods> unassignedGoods = goodsService.query().eq("status", "待委托").list();
        for (Goods goods : unassignedGoods) {
            Task task = new Task();
            //生成ID
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
            //更新货物状态(待委托 -> 已委托)
            goods.setStatus("已委托");
            goodsService.updateById(goods);
        }
        log.info("成功创建委托");
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
                //计算每辆车到起点的距离
                double distance = calculateDistance(car, poiService.getById(task.getStartId()));
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestCar = car;
                }
            }
            // 分配委托
            if (nearestCar != null) {
                task.setCarId(nearestCar.getId());
                // 更新委托状态(待接取 -> 待取货)
                task.setStatus("待取货");
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
     * 清空所有委托
     */
    @Override
    public void clearAllTasks() {
        this.remove(new QueryWrapper<>());
    }

    /**
     * 新增一个委托
     *zjm
     * @param taskDTO 委托信息
     */
    @Override
    public void addTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setId(Long.valueOf(taskDTO.getId()));
        task.setCarId(Long.valueOf(taskDTO.getCarId()));
        task.setGoodsId(Long.valueOf(taskDTO.getGoodsId()));
        task.setStartId(Long.valueOf(taskDTO.getStartId()));
        task.setEndId(Long.valueOf(taskDTO.getEndId()));
        task.setDistance(taskDTO.getDistance());
        task.setStatus(taskDTO.getStatus());
        this.save(task);
    }

    /**
     * 更新委托信息
     *
     * @param taskDTO 委托信息
     */
    @Override
    public void updateTask(TaskDTO taskDTO) {
        Task task = new Task();
        if (taskDTO.getId() == null) {
            throw new IllegalArgumentException("Task ID cannot be null");
        }
        task.setId(Long.valueOf(taskDTO.getId()));
        // 非空判断并赋予默认值
        task.setCarId(taskDTO.getCarId() != null ? Long.valueOf(taskDTO.getCarId()) : 0L);
        task.setGoodsId(taskDTO.getGoodsId() != null ? Long.valueOf(taskDTO.getGoodsId()) : 0L);
        task.setStartId(taskDTO.getStartId() != null ? Long.valueOf(taskDTO.getStartId()) : 0L);
        task.setEndId(taskDTO.getEndId() != null ? Long.valueOf(taskDTO.getEndId()) : 0L);
        task.setDistance(taskDTO.getDistance() != null ? taskDTO.getDistance() : 0.0);
        task.setStatus(taskDTO.getStatus() != null ? taskDTO.getStatus() : null);
        updateById(task);
        //goodsService.updateById(goodsService.getById(task.getGoodsId()));
        //carService.updateById(carService.getById(task.getCarId()));
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
