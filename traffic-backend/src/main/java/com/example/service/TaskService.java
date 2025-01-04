package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.TaskDTO;
import com.example.model.entity.Task;
import com.example.model.vo.TaskVO;

import java.util.List;

public interface TaskService extends IService<Task> {
    /**
     * 创建委托
     */
    void createTask();

    /**
     * 分配委托
     */
    void assignTask();

    /**
     * 将Task对象列表转换为TaskVO对象列表
     *
     * @param tasks Task对象列表
     * @return TaskVO对象列表
     */
    List<TaskVO> convertToTaskVO(List<Task> tasks);

    /**
     * 清空所有委托
     */
    void clearAllTasks();

    /**
     * 新增一条任务
     *
     * @param taskDTO 任务数据传输对象
     */
    void addTask(TaskDTO taskDTO);

    /**
     * 更新任务
     *
     * @param taskDTO 任务数据传输对象
     */
    void updateTask(TaskDTO taskDTO);
}
