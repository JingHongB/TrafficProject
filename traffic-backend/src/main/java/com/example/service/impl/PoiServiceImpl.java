package com.example.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.PoiMapper;
import com.example.model.entity.Poi;
import com.example.model.entity.PoiType;
import com.example.model.vo.PoiSearchVO;
import com.example.model.vo.PoiVO;
import com.example.service.PoiService;
import com.example.service.PoiTypeService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PoiServiceImpl extends ServiceImpl<PoiMapper, Poi> implements PoiService {
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private PoiTypeService poiTypeService;

    /**
     * 使用高德地图 API 搜索地点。
     *
     * @param city    搜索的城市
     * @param keyword 搜索的关键词
     * @param pageNum 页数
     * @return 地点信息列表
     */
    @Override
    public List<Poi> searchPoi(String city, String keyword, int pageNum) {
        List<Poi> locations = new ArrayList<>();
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
            locations.addAll(parseLocationData(response, keyword));
        }
        return locations;
    }

    /**
     * 将Poi转换为PoiSearchVO。
     *
     * @param poiList poi列表
     * @return PoiSearchVO列表
     */
    @Override
    public List<PoiSearchVO> PoiConvertToPoiSearchVO(List<Poi> poiList) {
        List<PoiSearchVO> poiSearchVOList = new ArrayList<>();
        for (Poi poi : poiList) {
            PoiSearchVO poiSearchVO = new PoiSearchVO();
            BeanUtils.copyProperties(poi, poiSearchVO);
            poiSearchVO.setId(String.valueOf(poi.getId()));
            poiSearchVOList.add(poiSearchVO);
        }
        return poiSearchVOList;
    }

    /**
     * 将Poi转换为PoiVO。
     *
     * @param poiList poi列表
     * @return PoiVO列表
     */
    @Override
    public List<PoiVO> PoiConvertToPoiVO(List<Poi> poiList) {
        List<PoiVO> poiVOList = new ArrayList<>();
        for (Poi poi : poiList) {
            PoiVO poiVO = new PoiVO();
            BeanUtils.copyProperties(poi, poiVO);
            poiVO.setTypeName(poiTypeService.getById(poi.getTypeId()).getName());
            poiVOList.add(poiVO);
        }
        return poiVOList;
    }

    /**
     * 解析高德地图API的JSON响应，提取地点数据。
     *
     * @param jsonResponse 高德地图API的JSON响应
     * @return 地点信息列表
     */
    private List<Poi> parseLocationData(String jsonResponse, String keyword) {
        List<Poi> locations = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(jsonResponse);
        if ("1".equals(jsonObject.getString("status"))) {
            JSONArray pois = jsonObject.getJSONArray("pois");
            for (int i = 0; i < pois.size(); i++) {
                JSONObject poi = pois.getJSONObject(i);
                try {
                    Poi entity = new Poi();
                    entity.setId(IdUtil.getSnowflakeNextId());
                    PoiType poiType = poiTypeService.query().eq("name", keyword).one();
                    // 如果数据库中存有该poi类型，则使用数据库中的id
                    if (poiType != null) {
                        entity.setTypeId(poiType.getId());
                    }
                    entity.setName(poi.getString("name"));
                    entity.setAddress(poi.getString("address"));
                    String[] coordinates = poi.getString("location").split(",");
                    entity.setLongitude(Double.valueOf(coordinates[0]));
                    entity.setLatitude(Double.valueOf(coordinates[1]));
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
