package com.example.model.common.handlerchain;

import com.example.model.common.exception.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author zrq
 * @time 2025/8/29 11:24
 * @description
 */
@Slf4j
@Component
public final class ChainFilterContext<T> implements ApplicationContextAware, CommandLineRunner {

    private ApplicationContext applicationContext;
    private final Map<String, List<AbstractChainFilter>> abstractChainFilterMap
            = new HashMap<>();

    public void handler(String mark, T requestParam) {
        if (!abstractChainFilterMap.containsKey(mark)) {
            throw new ClientException("没有对应的责任链组件--->" + mark);
        }
        abstractChainFilterMap.get(mark).forEach(filter -> {
            log.info("过滤器--{}--已经调用", filter);
            filter.handler(requestParam);
        });
    }

    @Override
    public void run(String... args) throws Exception {
        //name，filterBean
        Map<String, AbstractChainFilter> chainFilterMap = applicationContext.getBeansOfType(AbstractChainFilter.class);
        chainFilterMap.forEach((beanName, bean) -> {
            List<AbstractChainFilter> list = abstractChainFilterMap.getOrDefault(bean.mark(), new ArrayList<>());
            list.add(bean);
            abstractChainFilterMap.put(bean.mark(), list);
            log.info("{},过滤器已经注册", bean);
        });
        abstractChainFilterMap.forEach((beanMark, filterList) -> filterList.sort(Comparator.comparing(Ordered::getOrder)));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
