package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("task")
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long vehicleId;
    private Long goodsId;
    private String startPoint;
    private String endPoint;
    private String status;
}
