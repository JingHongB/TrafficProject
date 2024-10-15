package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Poi点类型
 */
@Data
@TableName("poi_type")
@AllArgsConstructor
@NoArgsConstructor
public class PoiType {
    @TableId
    private Long id;

    private String name;
    private String fatherNode;
    private String childNode;
}