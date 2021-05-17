package com.example.springbatch.xml;

import com.example.springbatch.pojo.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class WriteToXml {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private ItemReader<Product> readDataBaseData;
    @Autowired
    private ItemWriter<Product> writeToXmlWriter;


    @Bean
    public Job writeToXmlJob(){
        return jobBuilderFactory.get("writeToXmlJob")
                .start(writeToXmlStep())
                .build();
    }

    @Bean
    public Step writeToXmlStep() {
        return stepBuilderFactory.get("writeToXmlStep")
                .<Product,Product>chunk(4)
                .reader(readDataBaseData)
                .writer(writeToXmlWriter)
                .build();
    }
}
