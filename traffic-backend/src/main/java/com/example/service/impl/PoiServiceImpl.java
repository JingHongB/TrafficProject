package com.example.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.PoiMapper;
import com.example.model.dto.PoiDTO;
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
import java.util.Arrays;
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
            poiVO.setId(String.valueOf(poi.getId()));
            poiVO.setTypeName(poiTypeService.getById(poi.getTypeId()).getName());
            poiVOList.add(poiVO);
        }
        return poiVOList;
    }

    /**
     * 重置所有工厂状态
     */
    @Override
    public void resetAllFactoryStatus() {
        this.update()
                .set("status", "有货")
                .in("type_id", Arrays.asList(1, 5))  // type_id 为 1 或 5 的工厂状态设置为 有货
                .ne("status", "有货")  // 过滤掉已经是 "有货" 状态的记录
                .update();  // 执行更新操作

        // 对其他 type_id 的工厂状态修改为 "缺货"
        this.update()
                .set("status", "缺货")
                .notIn("type_id", Arrays.asList(1, 5))  // type_id 不是 1 或 5 的工厂状态设置为 缺货
                .ne("status", "缺货")  // 过滤掉已经是 "缺货" 状态的记录
                .update();  // 执行更新操作
    }


    /**
     * 添加一个POI
     *
     * @param poiDTO 地点信息
     */
    @Override
    public void addPoi(PoiDTO poiDTO) {
        Poi poi = new Poi();
        poi.setId(Long.parseLong(poiDTO.getId()));
        poi.setTypeId(Long.parseLong(poiDTO.getTypeId()));
        poi.setName(poiDTO.getName());
        poi.setAddress(poiDTO.getAddress());
        poi.setLongitude(Double.parseDouble(poiDTO.getLongitude()));
        poi.setLatitude(Double.parseDouble(poiDTO.getLatitude()));
        poi.setStatus(poiDTO.getStatus());
        save(poi);
    }

    /**
     * 更新一个POI
     *
     * @param poiDTO 地点信息
     */
    @Override
    public void updatePoi(PoiDTO poiDTO) {
        Poi poi = new Poi();
        if (poiDTO.getId() != null) {
            poi.setId(Long.parseLong(poiDTO.getId()));
        } else {
            poi.setId(0L); // 使用默认ID值
        }
        if (poiDTO.getTypeId() != null) {
            poi.setTypeId(Long.parseLong(poiDTO.getTypeId()));
        } else {
            poi.setTypeId(0L); // 使用默认TypeID值
        }
        if (poiDTO.getName() != null) {
            poi.setName(poiDTO.getName());
        } else {
            poi.setName(""); // 使用默认名称
        }
        if (poiDTO.getAddress() != null) {
            poi.setAddress(poiDTO.getAddress());
        } else {
            poi.setAddress(""); // 使用默认地址
        }
        if (poiDTO.getLongitude() != null) {
            poi.setLongitude(Double.parseDouble(poiDTO.getLongitude()));
        } else {
            poi.setLongitude(0.0); // 使用默认经度
        }
        if (poiDTO.getLatitude() != null) {
            poi.setLatitude(Double.parseDouble(poiDTO.getLatitude()));
        } else {
            poi.setLatitude(0.0); // 使用默认纬度
        }
        if (poiDTO.getStatus() != null) {
            poi.setStatus(poiDTO.getStatus());
        } else {
            poi.setStatus(""); // 使用默认状态
        }
        updateById(poi);
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
