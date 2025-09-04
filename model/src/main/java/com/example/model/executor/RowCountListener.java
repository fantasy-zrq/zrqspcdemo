package com.example.model.executor;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Getter;

/**
 * @author zrq
 * Excel 行数统计监听器
 */
public class RowCountListener extends AnalysisEventListener<Object> {

    @Getter
    private int rowCount = 0;

    @Override
    public void invoke(Object data, AnalysisContext context) {
        rowCount++;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // No additional actions needed after all data is analyzed
    }
}