package com.example.model.vo;

import lombok.Data;

/**
 * 返回给前端的搜索到的POI信息
 */
@Data
public class PoiSearchVO {
    private String id;
    private Long typeId;

    private String name;
    private String address;
    private Double longitude;
    private Double latitude;
}
