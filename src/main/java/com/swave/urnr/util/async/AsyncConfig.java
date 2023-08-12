package com.swave.urnr.util.async;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {
    @Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(25);
        taskExecutor.setMaxPoolSize(50);
        taskExecutor.setQueueCapacity(500);
        taskExecutor.setKeepAliveSeconds(30);
        taskExecutor.setThreadNamePrefix("Executor-");
        return taskExecutor;
    }
}