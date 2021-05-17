package com.example.springbatch.param;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 给任务添加参数
 */
@Configuration
@Slf4j
public class JobParameterDemo implements StepExecutionListener {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    private Map<String,JobParameter> parameters;


    @Bean
    public Job jobParam(){
        return jobBuilderFactory.get("jobParam")
                .start(paramStep())
                .build();
    }

    /**
     * 使用监听方式给step 传递参数
     * @return
     */
    @Bean
    public Step paramStep() {
        return stepBuilderFactory.get("paramStep")
                .listener(this)
                .tasklet((contribution, chunkContext) -> {
                    // 在idea哪里设置info=spring-batch才可以读取到？？？？
                    log.info("{}",parameters.get("info"));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        parameters = stepExecution.getJobParameters().getParameters();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
