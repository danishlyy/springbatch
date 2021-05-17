package com.example.springbatch.multi;

import com.example.springbatch.item.writer.MultiFileWriter;
import com.example.springbatch.pojo.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@Slf4j
public class MultiReaderDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Value("classpath:fund*.txt")
    private Resource[] resources;
    @Autowired
    private MultiFileWriter multiFileWriter;

    @Bean
    public Job multiReadJob(){
        return jobBuilderFactory.get("multiReadJob")
                .start(multiReadJobStep())
                .build();
    }

    @Bean
    public Step multiReadJobStep() {
        return stepBuilderFactory.get("multiReadJobStep")
                .<Product,Product>chunk(4)
                .reader(readMultiFile())
                .writer(multiFileWriter)
                .build();
    }

    @Bean
    public MultiResourceItemReader<Product> readMultiFile() {
        MultiResourceItemReader<Product> multiResourceItemReader = new MultiResourceItemReader<>();
        multiResourceItemReader.setResources(resources);
        multiResourceItemReader.setDelegate(txtMultiFileRead());

        return multiResourceItemReader;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Product> txtMultiFileRead() {
        FlatFileItemReader<Product> flatFileItemReader = new FlatFileItemReader<>();

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        DefaultLineMapper<Product> defaultLineMapper = new DefaultLineMapper<>();
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSet -> {
            Product product = new Product();
            product.setId(fieldSet.readLong(0));
            product.setProductName(fieldSet.readString(1));
            product.setProductCode(fieldSet.readString(2));
            product.setProductCompany(fieldSet.readString(3));
            return product;
        });
        defaultLineMapper.afterPropertiesSet();
        flatFileItemReader.setLineMapper(defaultLineMapper);
        return flatFileItemReader;
    }
}
