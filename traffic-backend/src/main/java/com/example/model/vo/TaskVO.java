package com.example.model.vo;

import lombok.Data;

@Data
public class TaskVO {
    private Long id;
    private Long vehicleId;
    private Long goodsId;
    private String startPoint;
    private String endPoint;
    private String status;

    private String startLongitude;
    private String startLatitude;
    private String endLongitude;
    private String endLatitude;
    private String vehicleLongitude;
    private String vehicleLatitude;
}
