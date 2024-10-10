package com.example.model.entity;

import lombok.Data;

/**
 * 所有地点实体的基类，包含通用字段。
 */
@Data
public class BaseLocationEntity {
    private String name;
    private String address;
    private String longitude;
    private String latitude;
}
