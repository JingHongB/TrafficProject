package com.example.controller;

import com.example.model.RestBean;
import com.example.service.DispatchScheduler;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transport")
@Tag(name = "运输", description = "运输相关操作")
@Slf4j
public class TransportController {
    @Resource
    DispatchScheduler dispatchScheduler;

    @PostMapping("/start")
    public RestBean<Void> startProject() {
        try {
            dispatchScheduler.startSimulation();
            return RestBean.success();
        } catch (Exception e) {
            log.error("项目启动失败", e);
            return RestBean.failure(500, "项目启动失败：" + e.getMessage());
        }
    }

    @PostMapping("/stop")
    public RestBean<Void> stopProject() {
        try {
            dispatchScheduler.stopSimulation();
            return RestBean.success();
        } catch (Exception e) {
            log.error("项目停止失败", e);
            return RestBean.failure(500, "项目停止失败：" + e.getMessage());
        }
    }
}
