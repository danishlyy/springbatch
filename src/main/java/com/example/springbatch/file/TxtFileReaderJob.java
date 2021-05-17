package com.example.springbatch.file;

import com.example.springbatch.item.writer.TxtFileWriter;
import com.example.springbatch.pojo.Product;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindException;

@Configuration
public class TxtFileReaderJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private TxtFileWriter txtFileWriter;


    @Bean
    public Job txtFileReadJob(){
        return jobBuilderFactory.get("txtFileReadJob")
                .start(txtFileReadStep())
                .build();
    }

    @Bean
    public Step txtFileReadStep() {
        return stepBuilderFactory.get("txtFileReadStep")
                .<Product,Product>chunk(4)
                .reader(txtFileRead())
                .writer(txtFileWriter)
                .build();
    }

    @Bean
    public FlatFileItemReader<Product> txtFileRead() {
        FlatFileItemReader<Product> flatFileItemReader = new FlatFileItemReader<>();
        ClassPathResource resource = new ClassPathResource("product.txt");
        flatFileItemReader.setResource(resource);
        flatFileItemReader.setLinesToSkip(1);

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames(new String[]{"id","productName","productCode","productCompany"});
        DefaultLineMapper<Product> defaultLineMapper = new DefaultLineMapper<>();
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(new FieldSetMapper<Product>() {
            @Override
            public Product mapFieldSet(FieldSet fieldSet) throws BindException {
                Product product = new Product();
                product.setId(fieldSet.readLong("id"));
                product.setProductName(fieldSet.readString("productName"));
                product.setProductCode(fieldSet.readString("productCode"));
                product.setProductCompany(fieldSet.readString("productCompany"));
                return product;
            }
        });
        defaultLineMapper.afterPropertiesSet();
        flatFileItemReader.setLineMapper(defaultLineMapper);
        return flatFileItemReader;
    }
}
