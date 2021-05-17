package com.example.springbatch.file;

import com.example.springbatch.pojo.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class WriteDataToMultiFilesClassify {

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

    @Bean
    public Job writeDataToMultiFilesClassifyJob(){
        return jobBuilderFactory.get("writeDataToMultiFilesClassifyJob")
                .start(writeDataToMultiFilesClassifyStep())
                .build();
    }

    @Bean
    public Step writeDataToMultiFilesClassifyStep() {
        return stepBuilderFactory.get("writeDataToMultiFilesClassifyStep")
                .<Product,Product>chunk(5)
                .reader(readDataBaseData)
                .writer(classifyWrite)
                .stream(writeDataToTxt)
                .stream(writeDataToXml)
                .build();
    }
}
