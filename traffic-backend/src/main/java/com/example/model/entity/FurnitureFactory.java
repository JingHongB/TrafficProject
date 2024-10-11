package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.model.BaseLocationEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 家具厂
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("furniture_factory")
@AllArgsConstructor
@NoArgsConstructor
public class FurnitureFactory extends BaseLocationEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
}
