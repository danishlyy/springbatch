package com.example.springbatch.config;

import com.example.springbatch.pojo.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.*;

@Configuration
@Slf4j
public class WriteDataToMultiFilesConfig {


    @Bean
    public CompositeItemWriter<Product> writeToMultiFiles() throws Exception {
        CompositeItemWriter<Product> writer = new CompositeItemWriter<>();
        writer.setDelegates(Arrays.asList(writeDataToTxtFile(),writeDataToXmlWriter()));
        writer.afterPropertiesSet();
        return writer;
    }

    @Bean
    public FlatFileItemWriter<Product> writeDataToTxtFile() throws Exception {
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

    @Bean
    public StaxEventItemWriter<Product> writeDataToXmlWriter() throws Exception {
        StaxEventItemWriter<Product> writer = new StaxEventItemWriter<>();
        XStreamMarshaller xStreamMarshaller = new XStreamMarshaller() ;
        Map<String,Class> aliases = new HashMap<>();
        aliases.put("product",Product.class);
        xStreamMarshaller.setAliases(aliases);
        writer.setRootTagName("products");
        writer.setMarshaller(xStreamMarshaller);
        String filePath = "/Users/yongyongli/reading/msad/product.xml";
        writer.setResource(new FileSystemResource(filePath));
        writer.afterPropertiesSet();
        return writer;
    }
}
