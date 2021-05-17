package com.example.springbatch.file;

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
public class WriteDataToMultiFiles {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private ItemReader<Product> readDataBaseData;
    @Autowired
    private ItemWriter<Product> writeToMultiFiles;

    @Bean
    public Job writeDataToMultiFilesJob(){
        return jobBuilderFactory.get("writeDataToMultiFilesJob")
                .start(writeDataToMultiFilesStep())
                .build();
    }

    @Bean
    public Step writeDataToMultiFilesStep() {
        return stepBuilderFactory.get("writeDataToMultiFilesStep")
                .<Product,Product>chunk(5)
                .reader(readDataBaseData)
                .writer(writeToMultiFiles)
                .build();
    }
}
