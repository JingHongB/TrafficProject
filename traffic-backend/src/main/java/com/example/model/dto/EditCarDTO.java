package com.example.model.dto;

import lombok.Data;

@Data
public class EditCarDTO {
    private Long id;
    private Long typeId;
    private Double longitude;
    private Double latitude;
}
