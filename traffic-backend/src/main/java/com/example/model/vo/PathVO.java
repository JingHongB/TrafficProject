package com.example.model.vo;

import lombok.Data;

@Data
public class PathVO {
    private String Longitude;
    private String Latitude;
    @Override
    public String toString() {
        return Longitude + ',' + Latitude;
    }
}
