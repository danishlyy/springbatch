/* 
  * Copyright By ZATI
  * Copyright By 3a3c88295d37870dfd3b25056092d1a9209824b256c341f2cdc296437f671617
  * All rights reserved.
  *
  * If you are not the intended user, you are hereby notified that any use, disclosure, copying, printing, forwarding or 
  * dissemination of this property is strictly prohibited. If you have got this file in error, delete it from your system. 
  */
package com.example.springbatch.decider;

import com.example.springbatch.decider.MyDecider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SpringBatchDeciderDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step deciderDemo1(){
        return stepBuilderFactory.get("deciderDemo1")
                .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED).build();
    }

    @Bean
    public Step deciderDemo2(){
        return stepBuilderFactory.get("deciderDemo2")
                .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED).build();
    }

    @Bean
    public Step deciderDemo3(){
        return stepBuilderFactory.get("deciderDemo3")
                .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED).build();
    }

    @Bean
    public JobExecutionDecider myJobExecutionDecider(){
        return new MyDecider();
    }

    @Bean
    public Job deciderJob(){
        return  jobBuilderFactory.get("deciderJob")
                .start(deciderDemo1())
                .next(myJobExecutionDecider())
                .from(myJobExecutionDecider()).on("even").to(deciderDemo2())
                .from(myJobExecutionDecider()).on("odd").to(deciderDemo3())
                .from(deciderDemo3()).on("*").to(myJobExecutionDecider())
                .end()
                .build();
    }
}
