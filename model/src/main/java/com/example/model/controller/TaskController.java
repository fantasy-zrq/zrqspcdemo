package com.example.model.controller;

import com.example.model.common.result.Result;
import com.example.model.common.result.Results;
import com.example.model.dto.req.TaskCreateReqDTO;
import com.example.model.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zrq
 * @time 2025/9/10 14:46
 * @description
 */
@RequiredArgsConstructor
@RestController
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/api/spc/model/task/create")
    public Result<String> createTask(@RequestBody TaskCreateReqDTO requestParam){
        taskService.createTask(requestParam);
        return Results.success("任务创建成功");
    }
}
