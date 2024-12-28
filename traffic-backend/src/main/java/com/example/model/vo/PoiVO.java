package com.example.model.vo;

import lombok.Data;

/**
 * 展示给前端的POI信息
 */
@Data
public class PoiVO {
    private String id;

    private String typeName;
    private String name;
    private String address;
    private Double longitude;
    private Double latitude;
    private String status;
}
