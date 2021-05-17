/* 
  * Copyright By ZATI
  * Copyright By 3a3c88295d37870dfd3b25056092d1a9209824b256c341f2cdc296437f671617
  * All rights reserved.
  *
  * If you are not the intended user, you are hereby notified that any use, disclosure, copying, printing, forwarding or 
  * dissemination of this property is strictly prohibited. If you have got this file in error, delete it from your system. 
  */
package com.example.springbatch.suc;

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
import org.springframework.core.task.SimpleAsyncTaskExecutor;


@Configuration
@Slf4j
public class SpiltDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;



    @Bean
    public Step spiltDemoStep1() {
        return stepBuilderFactory.get("spiltDemoStep1").tasklet((contribution, chunkContext) -> {
            log.info("spiltDemoStep1");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step spiltDemoStep2() {
        return stepBuilderFactory.get("spiltDemoStep2").tasklet((contribution, chunkContext) -> {
            log.info("spiltDemoStep2");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step spiltDemoStep3() {
        return stepBuilderFactory.get("spiltDemoStep3").tasklet((contribution, chunkContext) -> {
            log.info("spiltDemoStep3");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Flow spiltFlow1(){
        return new FlowBuilder<Flow>("spiltFlow1")
                .start(spiltDemoStep1())
                .next(spiltDemoStep2())
                .build();
    }

    @Bean
    public Flow spiltFlow2(){
        return new FlowBuilder<Flow>("spiltFlow2")
                .start(spiltDemoStep3())
                .build();
    }

    @Bean
    public Job fundManagerSpiltTask(){
        return jobBuilderFactory.get("fundManagerSpiltTask")
                .start(spiltFlow1())
                .split(new SimpleAsyncTaskExecutor())
                .add(spiltFlow2())
                .end()
                .build();
    }
}
