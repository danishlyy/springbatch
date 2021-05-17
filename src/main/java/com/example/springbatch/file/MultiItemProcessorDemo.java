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
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class MultiItemProcessorDemo {
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
    private ItemProcessor<Product,Product> dataProcessor;
    @Autowired
    private ItemProcessor<Product,Product> idFilterProcessor;

    @Bean
    public Job multiItemProcessorJob(){
        return jobBuilderFactory.get("multiItemProcessorJob")
                .start(multiItemProcessorStep())
                .build();
    }

    @Bean
    public Step multiItemProcessorStep() {
        return stepBuilderFactory.get("multiItemProcessorStep")
                .<Product,Product>chunk(4)
                .reader(readDataBaseData)
                .processor(processor())
                .writer(classifyWrite)
                .stream(writeDataToTxt)
                .stream(writeDataToXml)
                .build();
    }

    @Bean
    public CompositeItemProcessor<Product,Product> processor(){
        CompositeItemProcessor<Product,Product> compositeItemProcessor = new CompositeItemProcessor<>();
        List<ItemProcessor<Product,Product>> list = new ArrayList<>();
        list.add(idFilterProcessor);
        list.add(dataProcessor);
        compositeItemProcessor.setDelegates(list);
        return compositeItemProcessor;
    }
}
