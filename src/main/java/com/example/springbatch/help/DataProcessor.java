package com.example.springbatch.help;

import com.example.springbatch.pojo.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataProcessor implements ItemProcessor<Product,Product> {
    @Override
    public Product process(Product item) throws Exception {
        Product product = new Product();
        product.setId(item.getId());
        product.setProductName(item.getProductName() + "-新基金");
        product.setProductCompany(item.getProductCompany());
        product.setProductCode(item.getProductCode());
        return product;
    }
}
