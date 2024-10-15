package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Poi点
 */
@Data
@TableName("poi")
@AllArgsConstructor
@NoArgsConstructor
public class Poi {
    @TableId
    private Long id;
    private Long typeId;

    private String name;
    private String address;
    private Double longitude;
    private Double latitude;
    //默认设置为缺货
    private String status;
}
