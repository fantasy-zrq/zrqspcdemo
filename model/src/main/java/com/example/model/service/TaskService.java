package com.example.model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.req.TaskCreateReqDTO;
import com.example.model.entity.TaskDO;

public interface TaskService extends IService<TaskDO> {
    void createTask(TaskCreateReqDTO requestParam);
}
