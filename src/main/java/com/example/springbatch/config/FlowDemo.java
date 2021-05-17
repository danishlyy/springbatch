/* 
  * Copyright By ZATI
  * Copyright By 3a3c88295d37870dfd3b25056092d1a9209824b256c341f2cdc296437f671617
  * All rights reserved.
  *
  * If you are not the intended user, you are hereby notified that any use, disclosure, copying, printing, forwarding or 
  * dissemination of this property is strictly prohibited. If you have got this file in error, delete it from your system. 
  */
package com.example.springbatch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FlowDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step fundManagerInfo(){
        return stepBuilderFactory.get("fundManagerInfo").tasklet((contribution, chunkContext) -> {
            log.info("fundManagerInfo");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step fundManagerPhotoCompare(){
        return stepBuilderFactory.get("fundManagerPhotoCompare").tasklet((contribution, chunkContext) -> {
            log.info("fundManagerPhotoCompare");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step fundManagerProduct(){
        return stepBuilderFactory.get("fundManagerProduct").tasklet((contribution, chunkContext) -> {
            log.info("fundManagerProduct");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Flow fundManagerFlow(){
        return new FlowBuilder<Flow>("fundManagerFlow")
                .start(fundManagerInfo())
                .next(fundManagerPhotoCompare())
                .build();
    }

    @Bean
    public Job fundManagerJob(){
        return jobBuilderFactory.get("fundManagerJob")
                .start(fundManagerFlow())
                .next(fundManagerProduct())
                .end()
                .build();
    }
}
