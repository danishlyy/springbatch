package com.example.springbatch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@Slf4j
public class ListenerDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job listenerJob(){
        return jobBuilderFactory.get("listenerJob")
                .start(listenerJobStep1())
                .listener(new MyJobListener())
                .build();
    }

    @Bean
    public Step listenerJobStep1() {
        return stepBuilderFactory.get("listenerJobStep1")
                .<String,String>chunk(2)
                .faultTolerant()
                .listener(new MyChunkListener())
                .reader(read())
                .writer(write())
                .build();
    }

    @Bean
    public ItemWriter<String> write() {
        return items -> items.forEach(p->log.info("内容:{}",p));
    }

    @Bean
    public ItemReader<String> read() {
        return new ListItemReader<>(Arrays.asList("南方基金","易方达","汇添富基金"));
    }
}
