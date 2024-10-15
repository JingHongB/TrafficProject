package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Task;
import com.example.model.vo.TaskVO;

import java.util.List;

public interface TaskService extends IService<Task> {
    /**
     * 创建委托
     */
    void createTask();

    /**
     * 获取所有委托
     *
     * @return 委托列表
     */
    List<Task> getAllTasks();

    /**
     * 获取没有分配的委托
     *
     * @return 任务列表
     */
    List<Task> getUnassignedTasks();

    /**
     * 分配委托
     */
    void assignTask();

    /**
     * 将Task对象转换为TaskVO对象
     *
     * @param task Task对象
     * @return TaskVO对象列表
     */
    TaskVO convertToTaskVO(Task task);
}
