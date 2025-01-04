package com.example.model.dto;

import lombok.Data;

/**
 * 前端传给后端的车辆信息
 */
@Data
public class AddCarDTO {
    private String typeId;
    private Double longitude;
    private Double latitude;
}