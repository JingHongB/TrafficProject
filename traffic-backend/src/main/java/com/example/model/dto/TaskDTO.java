package com.example.model.dto;


import lombok.Data;

@Data
public class TaskDTO {
    private String id;
    private String CarId;
    private Double distance;
    private String status;
    private String goodsId;
    private String startId;
    private String endId;
}
