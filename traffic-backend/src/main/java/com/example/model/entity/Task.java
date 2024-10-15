package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务
 */
@Data
@TableName("task")
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @TableId
    private Long id;
    private Long CarId;
    private Long goodsId;
    private Long startId;
    private Long endId;

    private Double distance;
    private String status;
}
