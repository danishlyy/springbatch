
package com.example.springbatch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class BatchConfig {

    /**
     * 创建job
     */
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    /**
     * 创建 step
     */
    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job helloWorldJob(){
        return  jobBuilderFactory.get("helloWorldJob")
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").tasklet((stepContribution, chunkContext) -> {
            log.info("开始执行任务步骤:hello world");
            return RepeatStatus.FINISHED;
        }).build();
    }
}
