package com.example.model.dto;

import lombok.Data;

/**
 * 前端传给后端的POI信息
 */
@Data
public class PoiDTO {
    private String id;
    private String typeId;
    private String name;
    private String address;
    private String longitude;
    private String latitude;
    private String status;
}
