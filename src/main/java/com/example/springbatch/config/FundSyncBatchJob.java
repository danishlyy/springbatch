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
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FundSyncBatchJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job fundSyncBatchJobTask(){
        return jobBuilderFactory.get("fundSyncBatchJob")
                .start(fundBasicInfo())
                .next(fundExtendInfo())
                .next(fundManager()).build();
    }

    @Bean
    public Step fundManager() {
        return stepBuilderFactory.get("fundManager").tasklet((stepContribution, chunkContext) -> {
            log.info("fundManager");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step fundExtendInfo() {
        return stepBuilderFactory.get("fundExtend").tasklet((stepContribution, chunkContext) -> {
            log.info("查询基金产品扩展信息");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step fundBasicInfo() {
        return stepBuilderFactory.get("fundBasic").tasklet((stepContribution, chunkContext) -> {
            log.info("查询基金产品基本信息");
            return RepeatStatus.FINISHED;
        }).build();
    }
}
