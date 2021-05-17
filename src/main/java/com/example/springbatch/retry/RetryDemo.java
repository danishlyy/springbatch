package com.example.springbatch.retry;

import com.example.springbatch.exception.RetryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class RetryDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private ItemWriter<String> retryItemWriter;
    @Autowired
    private ItemProcessor<String,String> retryItemProcessor;

    @Bean
    public Job retryJob(){
        return jobBuilderFactory.get("retryJob")
                .start(retryStep())
                .build();
    }

    @Bean
    public Step retryStep() {
        return stepBuilderFactory.get("retryStep")
                .<String,String>chunk(10)
                .reader(readNumbers())
                .processor(retryItemProcessor)
                .writer(retryItemWriter)
                .faultTolerant()
                .retry(RetryException.class)
                .retryLimit(5)
                .build();
    }

    @Bean
    public ItemReader<String> readNumbers() {
        List<String> list = new ArrayList<>();
        for (int i=0;i<100;i++){
            list.add(String.valueOf(i));
        }
        return new ListItemReader<>(list);
    }
}
