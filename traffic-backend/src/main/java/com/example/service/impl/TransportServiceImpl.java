package com.example.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.model.entity.Car;
import com.example.model.entity.LatLng;
import com.example.model.entity.Poi;
import com.example.model.entity.Task;
import com.example.service.*;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TransportServiceImpl implements TransportService {
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private TaskService taskService;
    @Resource
    private CarService carService;
    @Resource
    private PoiService poiService;
    @Resource
    private GoodsService goodsService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    @Override
    public List<List<LatLng>> getRoutes(List<Task> tasks) {
        List<List<LatLng>> routes = new ArrayList<>();
        for (Task task : tasks) {
            Car car = carService.getById(task.getCarId());
            String carStatus = car.getStatus();
            if (carStatus.equals("空闲")) {
                Poi startPoi = poiService.getById(task.getStartId());
                List<LatLng> route = getRouteSegment(car.getLongitude(), car.getLatitude(), startPoi.getLongitude(), startPoi.getLatitude());
                carService.update().eq("id", car.getId()).set("status", "取货中").update();
                startCarMovement(car.getId(), route);
            }
        }
        return routes;
    }

    @Override
    public void startCarMovement(Long carId, List<LatLng> route) {
        Runnable task = new Runnable() {
            private int currentIndex = 0;

            @Override
            public void run() {
                if (currentIndex < route.size()) {
                    LatLng currentPosition = route.get(currentIndex);
                    // 更新车辆位置
                    carService.update().eq("id", carId).set("longitude", currentPosition.getLongitude()).set("latitude", currentPosition.getLatitude()).update();
                    currentIndex += 500;
                } else {
                    // 停止任务
                    if (carService.getById(carId).getStatus().equals("取货中")) {
                        carService.update().eq("id", carId).set("status", "装货").update();
                    }
                    scheduler.shutdown();
                }
            }
        };
        scheduler.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS); // 每 5 秒运行一次
    }

    private List<LatLng> getRouteSegment(Double fromLng, Double fromLat, Double toLng, Double toLat) {
        String url = String.format("%s?origin=%s,%s&destination=%s,%s&show_fields=polyline&key=%s",
                Const.AMAP_DIRECTION_URL, fromLng, fromLat, toLng, toLat, Const.API_KEY);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String body = response.getBody();
            // 解析返回的 JSON 数据并提取路径点
            return parseRouteFromJson(body);
        }

        return Collections.emptyList();
    }

    /**
     * 从 JSON 数据中解析路径信息
     *
     * @param json JSON 数据
     * @return 路径信息
     */
    private List<LatLng> parseRouteFromJson(String json) {
        List<LatLng> path = new ArrayList<>();
        try {
            // 解析 JSON 字符串
            JSONObject rootNode = JSON.parseObject(json);
            JSONObject routeNode = rootNode.getJSONObject("route");

            if (routeNode == null) {
                return path;
            }

            JSONArray pathsNode = routeNode.getJSONArray("paths");
            if (pathsNode != null) {
                for (int i = 0; i < pathsNode.size(); i++) {
                    JSONObject pathNode = pathsNode.getJSONObject(i);
                    JSONArray stepsNode = pathNode.getJSONArray("steps");
                    if (stepsNode != null) {
                        for (int j = 0; j < stepsNode.size(); j++) {
                            JSONObject stepNode = stepsNode.getJSONObject(j);
                            String polyline = stepNode.getString("polyline");
                            String[] coordinates = polyline.split(";");

                            for (String coordinate : coordinates) {
                                String[] latLng = coordinate.split(",");
                                if (latLng.length == 2) {
                                    double longitude = Double.parseDouble(latLng[0]);
                                    double latitude = Double.parseDouble(latLng[1]);
                                    path.add(new LatLng(longitude, latitude));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("解析路径信息失败", e);
        }
        return path;
    }
}
