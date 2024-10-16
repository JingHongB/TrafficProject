package com.example.model.vo;

import lombok.Data;

@Data
public class PoiSearchVO {
    private Long id;

    private String name;
    private String address;
    private Double longitude;
    private Double latitude;
}
