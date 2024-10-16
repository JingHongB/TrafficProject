package com.example.model.vo;

import lombok.Data;

@Data
public class CarVO {
    private Long id;
    private String typeName;
    private Double loadCapacity;
    private Double longitude;
    private Double latitude;
    private String status;
}
