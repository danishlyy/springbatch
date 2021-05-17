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
public class FundManagerBatchJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job fundManagerTask(){
        return jobBuilderFactory.get("fundManagerTask").start(fundManagerBasicInfo())
                .on("COMPLETED").to(fundManagerCompanyInfo())
                .from(fundManagerCompanyInfo()).on("COMPLETED").to(fundManagerProductInfo())
                .from(fundManagerProductInfo()).end()
                .build();
    }

    @Bean
    public Step fundManagerProductInfo() {
        return stepBuilderFactory.get("fundManagerProductInfo").tasklet((contribution, chunkContext) -> {
            log.info("fundManagerProductInfo");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step fundManagerCompanyInfo() {
        return stepBuilderFactory.get("fundManagerCompanyInfo").tasklet((contribution, chunkContext) -> {
            log.info("fundManagerCompanyInfo");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step fundManagerBasicInfo() {
        return stepBuilderFactory.get("fundManagerBasicInfo").tasklet((contribution, chunkContext) -> {
            log.info("fundManagerBasicInfo");
            return RepeatStatus.FINISHED;
        }).build();
    }
}
