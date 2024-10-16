package com.example.controller;

import com.example.model.RestBean;
import com.example.model.entity.Goods;
import com.example.model.entity.Task;
import com.example.model.vo.GoodsVO;
import com.example.model.vo.TaskVO;
import com.example.service.GoodsService;
import com.example.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transport")
@Tag(name = "运输", description = "运输相关操作")
@Slf4j
public class TransportController {
    @Resource
    private TaskService taskService;
    @Resource
    private GoodsService goodsService;

    @Operation(summary = "获取委托")
    @GetMapping("/task")
    public RestBean<List<TaskVO>> getTaskList() {
        try {
            List<Task> tasks = taskService.list();
            return RestBean.success(taskService.convertToTaskVO(tasks));
        } catch (Exception e) {
            log.info("获取委托失败", e);
            return RestBean.failure(500, "获取委托失败");
        }
    }

    @Operation(summary = "创建委托")
    @PostMapping("/task/create")
    public RestBean<Void> createTask() {
        try {
            taskService.createTask();
            return RestBean.success();
        } catch (Exception e) {
            log.error("创建委托失败", e);
            return RestBean.failure(500, "创建委托失败");
        }
    }

    @Operation(summary = "获取货物")
    @GetMapping("/goods")
    public RestBean<List<GoodsVO>> getGoodsList() {
        try {
            List<Goods> goodsList = goodsService.list();
            return RestBean.success(goodsService.convertToVO(goodsList));
        } catch (Exception e) {
            log.error("获取货物失败", e);
            return RestBean.failure(500, "获取货物失败");
        }
    }

    @Operation(summary = "创建货物")
    @PostMapping("/goods/create")
    public RestBean<Void> createGoods() {
        try {
            goodsService.createGoods();
            return RestBean.success();
        } catch (Exception e) {
            log.error("创建货物失败", e);
            return RestBean.failure(500, "创建货物失败");
        }
    }

    @Operation(summary = "分配委托")
    @PostMapping("/assign")
    public RestBean<Void> assignTask() {
        try {
            taskService.assignTask();
            return RestBean.success();
        } catch (Exception e) {
            return RestBean.failure(500, "分配委托失败");
        }
    }
}
