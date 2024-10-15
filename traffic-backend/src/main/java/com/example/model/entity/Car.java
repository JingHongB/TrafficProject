package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车辆
 */
@Data
@TableName("car")
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    @TableId
    private Long id;
    private Long typeId;

    private Double longitude;
    private Double latitude;
    //默认设置为空闲
    private String status;
}
