package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 货物
 */
@Data
@TableName("goods")
@AllArgsConstructor
@NoArgsConstructor
public class Goods {
    @TableId
    private Long id;
    private Long typeId;
    private Long ownerId;

    private Double weight;
    //默认设置为待委托
    private String status;
}
