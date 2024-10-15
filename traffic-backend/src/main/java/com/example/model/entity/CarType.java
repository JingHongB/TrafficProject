package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车辆类型
 */
@Data
@TableName("car_type")
@AllArgsConstructor
@NoArgsConstructor
public class CarType {
    @TableId
    private Long id;

    private String name;
    private Double load;
}
