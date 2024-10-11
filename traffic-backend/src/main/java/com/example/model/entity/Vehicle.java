package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车辆
 */
@Data
@TableName("vehicle")
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String longitude;
    private String latitude;
    private String status;
}
