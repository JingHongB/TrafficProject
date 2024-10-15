package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 货物类型
 */
@Data
@TableName("goods_type")
@AllArgsConstructor
@NoArgsConstructor
public class GoodsType {
    @TableId
    private Long id;

    private String name;
    private Double maxWeight;
    private Double minWeight;
    private String fatherNode;
    private String childNode;
    private Long time;
}
