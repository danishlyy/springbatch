package com.example.springbatch.xml;

import com.example.springbatch.item.writer.XmlFileWrite;
import com.example.springbatch.pojo.Product;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class XmlDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private XmlFileWrite xmlFileWrite;

    @Bean
    public Job xmlJob(){
        return jobBuilderFactory.get("xmlJob")
                .start(xmlJobStep())
                .build();
    }

    @Bean
    public Step xmlJobStep() {
        return stepBuilderFactory.get("xmlJobStep")
                .<Product,Product>chunk(2)
                .reader(xmlFileRead())
                .writer(xmlFileWrite)
                .build();
    }

    @Bean
    public StaxEventItemReader<Product> xmlFileRead() {
        StaxEventItemReader<Product> staxEventItemReader =new StaxEventItemReader<>();
        staxEventItemReader.setResource(new ClassPathResource("product.xml"));
        staxEventItemReader.setFragmentRootElementName("product");
        XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
        Map<String, Class> aliases = new HashMap<>();
        aliases.put("product",Product.class);
        xStreamMarshaller.setAliases(aliases);
        staxEventItemReader.setUnmarshaller(xStreamMarshaller);
        return staxEventItemReader;
    }
}
