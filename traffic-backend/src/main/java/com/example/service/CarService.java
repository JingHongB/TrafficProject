package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.AddCarDTO;
import com.example.model.dto.EditCarDTO;
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

    /**
     * 删除Car
     */
    void deleteCar(Long id);

    /**
     * 新增一辆车
     */
    void addCar(AddCarDTO addCarDTO);

    /**
     * 更新一辆车
     */
    void updateCar(EditCarDTO editCarDTO);

    /**
     * 根据id获取车辆信息
     */
    CarVO getCarById(long id);
}
