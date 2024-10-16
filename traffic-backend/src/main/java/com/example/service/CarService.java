package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Car;
import com.example.model.vo.CarVO;

import java.util.List;

public interface CarService extends IService<Car> {

    /**
     * 初始化车辆信息
     */
    void initializeCars();

    /**
     * 获取CarVO列表
     *
     * @return CarVO列表
     */
    List<CarVO> getCarVOList();
}
