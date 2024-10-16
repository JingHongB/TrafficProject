package com.example.model.vo;

import lombok.Data;

@Data
public class TaskVO {
    private String id;
    private String CarId;
    private Double distance;
    private String status;

    private String goods;
    private String startPoint;
    private String endPoint;
    private Double startLongitude;
    private Double startLatitude;
    private Double endLongitude;
    private Double endLatitude;
    private Double CarLongitude;
    private Double CarLatitude;
}
