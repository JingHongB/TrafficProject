package com.example.model.vo;

import lombok.Data;

/**
 * 展示给前端的车辆信息
 */
@Data
public class CarVO {
    private String id;
    private String typeName;
    private Double loadCapacity;
    private Double longitude;
    private Double latitude;
    private String status;
}
