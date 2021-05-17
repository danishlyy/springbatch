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

@Configuration
@Slf4j
public class ChildJobTwo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step ChildJobTwoStep1(){
        return stepBuilderFactory.get("ChildJobTwoStep1").tasklet((contribution, chunkContext) -> {
            log.info("ChildJobTwoStep1");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step ChildJobTwoStep2(){
        return stepBuilderFactory.get("ChildJobTwoStep2").tasklet((contribution, chunkContext) -> {
            log.info("ChildJobTwoStep2");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Job ChildJobTwoJob(){
        return jobBuilderFactory.get("ChildJobTwoJob")
                .start(ChildJobTwoStep1())
                .next(ChildJobTwoStep2())
                .build();
    }
}
