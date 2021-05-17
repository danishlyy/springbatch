package com.example.springbatch.config;

import com.example.springbatch.pojo.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@Slf4j
public class ReadTxtFileConfig {
    ItemReader

    @Bean
    public FlatFileItemReader<Product> readTxtFile() {
        FlatFileItemReader<Product> flatFileItemReader = new FlatFileItemReader<>();
        ClassPathResource resource = new ClassPathResource("product.txt");
        flatFileItemReader.setResource(resource);
        flatFileItemReader.setLinesToSkip(1);

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames(new String[]{"id","productName","productCode","productCompany"});
        DefaultLineMapper<Product> defaultLineMapper = new DefaultLineMapper<>();
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSet -> {
            Product product = new Product();
            product.setId(fieldSet.readLong("id"));
            product.setProductName(fieldSet.readString("productName"));
            product.setProductCode(fieldSet.readString("productCode"));
            product.setProductCompany(fieldSet.readString("productCompany"));
            return product;
        });
        defaultLineMapper.afterPropertiesSet();
        flatFileItemReader.setLineMapper(defaultLineMapper);
        return flatFileItemReader;
    }
}
