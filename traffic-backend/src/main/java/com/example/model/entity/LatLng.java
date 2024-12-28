package com.example.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 经纬度
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LatLng {
    private double longitude;
    private double latitude;
}
