package com.example.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.model.BaseLocationEntity;
import com.example.service.PoiService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class PoiServiceImpl implements PoiService {
    @Resource
    private RestTemplate restTemplate;

    /**
     * 使用高德地图 API 搜索地点。
     *
     * @param city    搜索的城市
     * @param keyword 搜索的关键词
     * @param pageNum 页数
     * @return 地点信息列表
     */
    @Override
    public List<BaseLocationEntity> searchPoi(String city, String keyword, int pageNum) {
        List<BaseLocationEntity> locations = new ArrayList<>();
        // 设置查询参数
        URI uri = UriComponentsBuilder.fromHttpUrl(Const.AMAP_SEARCH_API)
                .queryParam("key", Const.API_KEY)
                .queryParam("keywords", keyword)
                .queryParam("city", city)
                .queryParam("offset", "25")
                .queryParam("page", pageNum)
                .queryParam("output", "JSON")
                .build().encode().toUri();
        // 发起 GET 请求
        String response = restTemplate.getForObject(uri, String.class);
        if (response != null) {
            locations.addAll(parseLocationData(response));
        }
        return locations;
    }

    /**
     * 解析高德地图API的JSON响应，提取地点数据。
     *
     * @param jsonResponse 高德地图API的JSON响应
     * @return 地点信息列表
     */
    private List<BaseLocationEntity> parseLocationData(String jsonResponse) {
        List<BaseLocationEntity> locations = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(jsonResponse);
        if ("1".equals(jsonObject.getString("status"))) {
            JSONArray pois = jsonObject.getJSONArray("pois");
            for (int i = 0; i < pois.size(); i++) {
                JSONObject poi = pois.getJSONObject(i);
                try {
                    BaseLocationEntity entity = new BaseLocationEntity();
                    entity.setName(poi.getString("name"));
                    entity.setAddress(poi.getString("address"));
                    String[] coordinates = poi.getString("location").split(",");
                    entity.setLongitude(coordinates[0]);
                    entity.setLatitude(coordinates[1]);
                    locations.add(entity);
                } catch (Exception e) {
                    System.out.println("对象创建失败: " + e.getMessage());
                }
            }
        } else {
            System.out.println("请求失败，错误信息: " + jsonObject.getString("info"));
        }
        return locations;
    }
}
