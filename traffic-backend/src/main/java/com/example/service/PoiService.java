package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Poi;

import java.util.List;

public interface PoiService extends IService<Poi> {
    /**
     * 根据城市、关键词和页数，通过高德API进行搜索。
     *
     * @param city    搜索的城市
     * @param keyword 搜索的关键词
     * @param pageNum 页数
     * @return 搜索结果
     */
    List<?> searchPoi(String city, String keyword, int pageNum);
}
