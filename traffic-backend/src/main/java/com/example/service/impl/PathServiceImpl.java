package com.example.service.impl;

import com.example.mapper.PathMapper;
import com.example.mapper.VehicleMapper;
import com.example.model.vo.PathVO;
import com.example.service.PathService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class PathServiceImpl implements PathService {
    @Resource
    private VehicleMapper vehicleMapper;
    @Resource
    private PathMapper pathMapper;
    private static final String API_KEY = "f6de21f5d7559ebdc0cbd2b02f35440a";
    private static final String BASE_URL = "https://restapi.amap.com/v3/direction/driving";
    /**
     * 获取两点之间路径
     */
    @Override
    public String getPath() {
        PathVO originVO = pathMapper.getOrigin();
        PathVO destinationVO = pathMapper.getDestination();
        String origin = originVO.toString();
        String destination = destinationVO.toString();

        String url = BASE_URL + "?key=" + API_KEY + "&origin=" + origin + "&destination=" + destination;
        RestTemplate restTemplate = new RestTemplate();
        String pathStr = restTemplate.getForObject(url, String.class);
        return pathStr;
    }
}
