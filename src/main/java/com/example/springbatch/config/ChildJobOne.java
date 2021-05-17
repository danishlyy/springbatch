
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
public class ChildJobOne {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step ChildJobOneStep1(){
        return stepBuilderFactory.get("ChildJobOneStep1").tasklet((contribution, chunkContext) -> {
            log.info("ChildJobOneStep1");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Job ChildJobOneJob(){
        return jobBuilderFactory.get("ChildJobOneJob")
                .start(ChildJobOneStep1())
                .build();
    }
}
