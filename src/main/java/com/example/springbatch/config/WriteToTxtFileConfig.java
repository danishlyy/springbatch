package com.example.springbatch.config;

import com.example.springbatch.pojo.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@Slf4j
public class WriteToTxtFileConfig {

    @Bean
    public FlatFileItemWriter<Product> writeToTxtFile() throws Exception {
        FlatFileItemWriter<Product> writer = new FlatFileItemWriter<>();
        String filePath = "/Users/yongyongli/reading/msad/product.txt";
        writer.setResource(new FileSystemResource(filePath));
        writer.setLineAggregator(item -> {
            ObjectMapper objectMapper = new ObjectMapper();
            String result = null;
            try {
                result = objectMapper.writeValueAsString(item);
            } catch (JsonProcessingException e) {
                log.error("异常",e);
            }
            return result;
        });
        writer.afterPropertiesSet();
        return writer;
    }
}
