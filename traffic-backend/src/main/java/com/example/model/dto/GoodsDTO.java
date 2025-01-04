package com.example.model.dto;

import lombok.Data;

/**
 * 前端传给后端的货物信息
 */
@Data
public class GoodsDTO {
    private String id;
    private String typeId;
    private String ownerId;
    private Double weight;
    private String status;
}
