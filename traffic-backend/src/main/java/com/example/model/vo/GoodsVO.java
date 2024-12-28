package com.example.model.vo;

import lombok.Data;

/**
 * 展示给前端你的货物信息
 */
@Data
public class GoodsVO {
    private String id;
    private String type;
    private String owner;
    private Double weight;
    private String status;
}
