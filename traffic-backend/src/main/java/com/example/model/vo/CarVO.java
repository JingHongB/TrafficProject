package com.example.model.vo;

import lombok.Data;

/**
 * 返回给前端的车辆信息，额外包括车辆类型名称、载重量
 */
@Data
public class CarVO {
    private Long id;
    private String typeName;
    private Double loadCapacity;
    private Double longitude;
    private Double latitude;
    private String status;
}
