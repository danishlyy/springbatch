package com.example.springbatch.restart;

import com.example.springbatch.item.reader.RestartReader;
import com.example.springbatch.item.writer.RestartWriter;
import com.example.springbatch.pojo.Product;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestartDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private RestartReader restartReader;
    @Autowired
    private RestartWriter restartWriter;

    @Bean
    public Job restartJob(){
        return jobBuilderFactory.get("restartJob")
                .start(restartStep())
                .build();
    }

    @Bean
    public Step restartStep() {
        return stepBuilderFactory.get("restartStep")
                .<Product,Product>chunk(10)
                .reader(restartReader)
                .writer(restartWriter)
                .build();
    }
}
