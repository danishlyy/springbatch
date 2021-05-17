package com.example.springbatch.config;

import com.example.springbatch.pojo.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;


@Configuration
@Slf4j
public class WriteToXmlWriterConfig {

    @Bean
    public StaxEventItemWriter<Product> writeToXmlWriter() throws Exception {
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
