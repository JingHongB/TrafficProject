package com.example.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.CarMapper;
import com.example.model.entity.Car;
import com.example.model.vo.CarVO;
import com.example.service.CarService;
import com.example.service.CarTypeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements CarService {
    @Resource
    private CarTypeService carTypeService;

    /**
     * 初始化车辆
     */
    @Override
    @Transactional
    public void initializeCars() {
        //清空车辆表
        this.remove(new QueryWrapper<>());
        //获取车辆类型数量
        long carTypeCount = carTypeService.count();
        List<Car> cars = new ArrayList<>();
        Random random = new Random();
        //生成5辆车
        for (int i = 0; i < 10; i++) {
            Car car = new Car();
            car.setStatus("空闲");
            //雪花算法生成唯一ID
            car.setId(IdUtil.getSnowflakeNextId());
            car.setTypeId(random.nextLong(carTypeCount) + 1);  // 随机生成车辆类型
            //车辆大致生成在四川省境内
            // 生成车辆经度和纬度，并控制精度为小数点后六位
            double longitude = 97.35 + (108.54 - 97.35) * random.nextDouble();
            double latitude = 26.05 + (34.31 - 26.05) * random.nextDouble();
            car.setLongitude(Math.round(longitude * 1_000_000) / 1_000_000.0); // 控制经度的精度
            car.setLatitude(Math.round(latitude * 1_000_000) / 1_000_000.0); // 控制纬度的精度
            cars.add(car);
        }
        //插入数据
        this.saveBatch(cars);
        log.info("车辆信息初始化完成");
    }

    /**
     * 获取CarVO列表
     *
     * @return CarVO列表
     */
    @Override
    public List<CarVO> getCarVOList() {
        List<Car> cars = list();
        List<CarVO> carVOS = new ArrayList<>();
        for (Car car : cars) {
            carVOS.add(convertToCarVO(car));
        }
        return carVOS;
    }

    /**
     * 将Car转换为CarVO
     *
     * @param car 车辆信息
     * @return 车辆VO信息
     */
    private CarVO convertToCarVO(Car car) {
        CarVO carVO = new CarVO();
        BeanUtils.copyProperties(car, carVO);
        carVO.setId(String.valueOf(car.getId()));
        carVO.setTypeName(carTypeService.getById(car.getTypeId()).getName());
        carVO.setLoadCapacity(carTypeService.getById(car.getTypeId()).getLoadCapacity());
        return carVO;
    }
}
