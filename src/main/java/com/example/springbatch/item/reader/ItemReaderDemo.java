package com.example.springbatch.item.reader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
public class ItemReaderDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job itemReadJob(){
        return jobBuilderFactory.get("itemReadJob")
                .start(itemReadSetp())
                .build();
    }

    @Bean
    public Step itemReadSetp() {
        return stepBuilderFactory.get("itemReadSetp")
                .<String,String>chunk(2)
                .reader(readData())
                .writer(list->list.forEach(p->log.info("结果:{}",p)))
                .build();
    }



    @Bean
    public ReadData readData() {
        List<String> data = Arrays.asList("java","python","go","vue");
        return  new ReadData(data);
    }
}
