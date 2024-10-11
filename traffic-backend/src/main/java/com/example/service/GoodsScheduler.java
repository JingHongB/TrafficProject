package com.example.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GoodsScheduler {
    @Resource
    private GoodsService goodsService;

    /**
     * 定时任务：每隔 60 秒生成一次货物
     */
    @Scheduled(fixedRate = 60000)
    public void createGoods() {
        log.info("创建货物");
        goodsService.createGoods();
    }
}
