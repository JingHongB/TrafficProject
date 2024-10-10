package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 该类用于存储伐木场的基本信息，如名称、地址和地理位置。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("lumberyard")
@AllArgsConstructor
@NoArgsConstructor
public class Lumberyard extends BaseLocationEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
}
