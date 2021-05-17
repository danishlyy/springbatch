package com.example.springbatch.file;

import com.example.springbatch.pojo.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ItemProcessorDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private ItemReader<Product> readDataBaseData;
    @Autowired
    private ItemWriter<Product> classifyWrite;
    @Autowired
    private ItemStreamWriter<Product> writeDataToXml;
    @Autowired
    private ItemStreamWriter<Product> writeDataToTxt;
    @Autowired
    private ItemProcessor<? super Product, ? extends Product> dataProcessor;

    @Bean
    public Job itemProcessorJob(){
        return jobBuilderFactory.get("itemProcessorJob")
                .start(itemProcessorStep())
                .build();
    }

    @Bean
    public Step itemProcessorStep() {
        return stepBuilderFactory.get("itemProcessorStep")
                .<Product,Product>chunk(4)
                .reader(readDataBaseData)
                .processor(dataProcessor)
                .writer(classifyWrite)
                .stream(writeDataToTxt)
                .stream(writeDataToXml)
                .build();
    }
}
