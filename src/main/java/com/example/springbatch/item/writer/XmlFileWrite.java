package com.example.springbatch.item.writer;

import com.example.springbatch.pojo.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class XmlFileWrite implements ItemWriter<Product> {

    @Override
    public void write(List<? extends Product> items) throws Exception {
        items.forEach(p->log.info("基金产品信息:{}",p));
    }
}
