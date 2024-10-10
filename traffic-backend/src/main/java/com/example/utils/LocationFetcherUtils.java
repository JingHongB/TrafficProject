package com.example.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.model.entity.BaseLocationEntity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Component
public class LocationFetcherUtils {
    @Resource
    private RestTemplate restTemplate;

    /**
     * 从高德地图API获取指定地点类型的数据。
     *
     * @param keywords 查询的关键字
     * @param clazz    实体类的类型
     * @return 地点信息列表
     */
    public <T> List<T> fetchLocationData(String keywords, Class<T> clazz) {
        List<T> locations = new ArrayList<>();
        // 设置查询参数
        URI uri = UriComponentsBuilder.fromHttpUrl(Const.AMAP_SEARCH_API)
                .queryParam("key", Const.API_KEY)
                .queryParam("keywords", keywords)
                .queryParam("city", "四川省")
                .queryParam("offset", "25")
                .queryParam("page", "1")
                .queryParam("output", "JSON")
                .build().encode().toUri();
        // 发起 GET 请求
        String response = restTemplate.getForObject(uri, String.class);
        if (response != null) {
            locations.addAll(parseLocationData(response, clazz));
        }
        return locations;
    }

    /**
     * 解析高德地图API的JSON响应，提取地点数据。
     *
     * @param jsonResponse 高德地图API的JSON响应
     * @param clazz        实体类的类型
     * @return 地点信息列表
     */
    private <T> List<T> parseLocationData(String jsonResponse, Class<T> clazz) {
        List<T> locations = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(jsonResponse);
        if ("1".equals(jsonObject.getString("status"))) {
            JSONArray pois = jsonObject.getJSONArray("pois");
            for (int i = 0; i < pois.size(); i++) {
                JSONObject poi = pois.getJSONObject(i);
                try {
                    T location = clazz.getDeclaredConstructor().newInstance();
                    if (location instanceof BaseLocationEntity entity) {
                        entity.setName(poi.getString("name"));
                        entity.setAddress(poi.getString("address"));
                        String[] coordinates = poi.getString("location").split(",");
                        entity.setLongitude(coordinates[0]);
                        entity.setLatitude(coordinates[1]);
                    }
                    locations.add(location);
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
