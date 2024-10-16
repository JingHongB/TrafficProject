package com.example.model.vo;

import lombok.Data;

@Data
public class GoodsVO {
    private Long id;
    private String type;
    private String owner;
    private Double weight;
    private String status;
}
