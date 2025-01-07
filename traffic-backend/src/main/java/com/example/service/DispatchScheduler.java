package com.example.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DispatchScheduler {
    @Resource
    private TaskService taskService;
    @Resource
    private GoodsService goodsService;

    private ScheduledExecutorService scheduler;
    private boolean running = false;

    public void startSimulation() {
        if (running) {
            return; // 防止重复启动
        }
        scheduler = Executors.newScheduledThreadPool(1);
        running = true;

        scheduler.scheduleAtFixedRate(this::startProject, 0, 5, TimeUnit.SECONDS);
    }

    public void stopSimulation() {
        if (scheduler != null) {
            scheduler.shutdown();
            running = false;
            log.info("结束调度");
        }
    }

    public void startProject() {
        log.info("开始调度");
        try {
            goodsService.createGoods();
            taskService.createTask();
            taskService.assignTask();
        } catch (Exception e) {
            log.error("调度失败", e);
        }
    }
}

