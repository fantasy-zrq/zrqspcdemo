package com.example.model.executor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.example.model.dto.req.ExcelRedisReqDTO;
import com.example.model.dto.req.ExcelReqDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zrq
 * @time 2025/9/4 15:18
 * @description
 */
@Component
@RequiredArgsConstructor
@Slf4j(topic = "ExcelResolverThreadPool")
public class ExcelResolverThreadPool {

    private final StringRedisTemplate stringRedisTemplate;

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(2,
            5,
            500L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(5),
            runnable -> {
                Thread thread = new Thread(runnable);
                thread.setName("thread-pool-mock-excel");
                return thread;
            }
    );

    public void execute(ExcelReqDTO requestParam) {
        log.info("ExcelResolverThreadPool 开始执行任务---入参-->{}", requestParam);
        Optional.ofNullable(requestParam).orElseThrow(NullPointerException::new);
        Runnable task = () -> {
            RowCountListener listener = new RowCountListener();
            EasyExcel.read(requestParam.getFileAddr(), listener).sheet().doRead();
            int totalRows = listener.getRowCount();
            ExcelRedisReqDTO excelRedisReqDTO = BeanUtil.copyProperties(requestParam, ExcelRedisReqDTO.class);
            excelRedisReqDTO.setRowCount(totalRows);
            stringRedisTemplate.opsForValue().set("zrq:spc:mock:excel:rowcount",
                    JSONUtil.toJsonStr(excelRedisReqDTO));
        };
        executor.execute(task);
    }
}
