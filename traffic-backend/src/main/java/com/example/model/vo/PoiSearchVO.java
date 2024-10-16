package com.example.model.vo;

import lombok.Data;

@Data
public class PoiSearchVO {
    private String id;
    private Long typeId;

    private String name;
    private String address;
    private Double longitude;
    private Double latitude;
}
