package com.example.model.dto;

import lombok.Data;

/**
 * 接收前端搜索请求的数据传输对象
 */
@Data
public class SearchRequestDTO {
    // 搜索的城市
    private String city;
    // 搜索的关键词
    private String keyword;
    // 页码
    private int pageNum;
}
