package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 伐木场
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