package com.example.model.dto;

import lombok.Data;

@Data
public class SearchRequestDTO {
    private String city;
    private String keyword;
    private int pageNum;
}
