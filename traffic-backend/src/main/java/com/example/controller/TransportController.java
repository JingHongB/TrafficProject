package com.example.controller;

import com.example.model.RestBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transport")
@Tag(name = "运输", description = "运输相关操作")
@Slf4j
public class TransportController {

    /**
     * 启动模拟运输
     *
     * @return Void
     */
    @Operation(summary = "启动模拟运输")
    @PostMapping("/start")
    public RestBean<Void> startProject() {
        try {
            return RestBean.success();
        } catch (Exception e) {
            log.error("开始运输失败", e);
            return RestBean.failure(500, "开始运输失败");
        }
    }
}
