package com.example.springbatch.database;

import com.example.springbatch.pojo.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ItemWriteToDataBase {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private ItemReader<Product> readTxtFile;
    @Autowired
    private ItemWriter<Product> writeContents;

    @Bean
    public Job itemWriteToMysql(){
        return jobBuilderFactory.get("itemWriteToMysql")
                .start(writeToMysqlStep())
                .build();
    }

    @Bean
    public Step writeToMysqlStep() {
        return stepBuilderFactory.get("writeToMysqlStep")
                .<Product,Product>chunk(2)
                .reader(readTxtFile)
                .writer(writeContents)
                .build();
    }


}
