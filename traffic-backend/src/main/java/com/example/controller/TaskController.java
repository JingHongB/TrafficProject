package com.example.controller;

import com.example.model.RestBean;
import com.example.model.dto.TaskDTO;
import com.example.model.entity.Task;
import com.example.model.vo.TaskVO;
import com.example.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
@Tag(name = "委托", description = "委托相关操作")
@Slf4j
public class TaskController {
    @Resource
    private TaskService taskService;

    /**
     * 创建委托
     *
     * @return Void
     */
    @Operation(summary = "创建委托")
    @PostMapping("/create")
    public RestBean<Void> createTask() {
        try {
            taskService.createTask();
            return RestBean.success();
        } catch (Exception e) {
            log.error("创建委托失败", e);
            return RestBean.failure(500, "创建委托失败");
        }
    }

    /**
     * 分配委托
     *
     * @return Void
     */
    @Operation(summary = "分配委托")
    @PostMapping("/assign")
    public RestBean<Void> assignTask() {
        try {
            taskService.assignTask();
            return RestBean.success();
        } catch (Exception e) {
            log.error("分配委托失败", e);
            return RestBean.failure(500, "分配委托失败");
        }
    }

    /**
     * 获取所有委托信息
     *
     * @return 所有委托列表
     */
    @Operation(summary = "获取委托")
    @GetMapping("/list")
    public RestBean<List<TaskVO>> getTaskList() {
        try {
            List<Task> tasks = taskService.list();
            return RestBean.success(taskService.convertToTaskVO(tasks));
        } catch (Exception e) {
            log.info("获取委托失败", e);
            return RestBean.failure(500, "获取委托失败");
        }
    }

    /**
     * 删除委托
     *
     * @param id 委托id
     * @return Void
     */
    @Operation(summary = "删除委托")
    @PostMapping("/delete/{id}")
    public RestBean<Void> deleteTask(@PathVariable("id") long id) {
        try {
            taskService.removeById(id);
            return RestBean.success();
        } catch (Exception e) {
            log.error("删除委托失败", e);
            return RestBean.failure(500, "删除委托失败");
        }
    }

    /**
     * 新增委托
     *
     * @param taskDTO 委托信息
     * @return Void
     */
    @Operation(summary = "新增委托")
    @PostMapping("/add")
    public RestBean<Void> addTask(@RequestBody TaskDTO taskDTO) {
        try {
            taskService.addTask(taskDTO);
            return RestBean.success();
        } catch (Exception e) {
            log.error("新增委托失败", e);
            return RestBean.failure(500, "新增委托失败");
        }
    }
    /**
     * 根据id查询委托
     * @param id 委托id
     * @return 任务信息
     */
    @Operation(summary = "根据id查询委托")
    @GetMapping("/get/{id}")
    public RestBean<TaskVO> getTaskById(@PathVariable("id") long id) {
        try {
            Task task = taskService.getById(id);
            return RestBean.success(taskService.convertToTaskVO(List.of(task)).get(0));
        } catch (Exception e) {
            log.error("根据id查询委托失败", e);
            return RestBean.failure(500, "根据id查询委托失败");
        }
    }

    /**
     * 更新委托
     * @param taskDTO 委托信息
     * @return Void
     */
    @Operation(summary = "更新委托")
    @PutMapping("/update")
    public RestBean<Void> updateTask(@RequestBody TaskDTO taskDTO) {
        try {
            taskService.updateTask(taskDTO);
            return RestBean.success();
        } catch (Exception e) {
            log.error("更新委托失败", e);
            return RestBean.failure(500, "更新委托失败");
        }
    }
}
