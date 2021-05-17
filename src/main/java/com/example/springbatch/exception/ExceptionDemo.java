package com.example.springbatch.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@Slf4j
public class ExceptionDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job exceptionJob(){
        return jobBuilderFactory.get("exceptionJob")
                .start(exceptionStep1())
                .next(exceptionStep2())
                .build();
    }

    @Bean
    public Step exceptionStep2() {
        return stepBuilderFactory.get("exceptionStep2")
                .tasklet(exceptionHandle())
                .build();
    }

    @Bean
    public Tasklet exceptionHandle() {
        return (contribution, chunkContext) -> {
            Map<String, Object> stepExecutionContext = chunkContext.getStepContext().getStepExecutionContext();
            if (stepExecutionContext.containsKey("fund")){
                log.info("job second run success");
                return RepeatStatus.FINISHED;
            }else {
                log.info("job first run fail");
                chunkContext.getStepContext().getStepExecution().getExecutionContext().put("fund",true);
                throw new RuntimeException();
            }
        };
    }

    @Bean
    public Step exceptionStep1() {
        return stepBuilderFactory.get("exceptionStep1")
                .tasklet(exceptionHandle())
                .build();
    }
}
