package com.example.springbatch.config;

import com.example.springbatch.pojo.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class ReadDataBaseDataConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcPagingItemReader<Product> readDataBaseData(){
        JdbcPagingItemReader<Product> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setFetchSize(5);
        reader.setRowMapper((resultSet, rowNumber) -> {
            Product product = new Product();
            product.setId(resultSet.getLong("id"));
            product.setProductCode(resultSet.getString("product_code"));
            product.setProductName(resultSet.getString("product_name"));
            product.setProductCompany(resultSet.getString("product_company"));
            return product;
        });
        MySqlPagingQueryProvider mySqlPagingQueryProvider = new MySqlPagingQueryProvider();
        mySqlPagingQueryProvider.setSelectClause("select id,product_code,product_name,product_company");
        mySqlPagingQueryProvider.setFromClause("from product");
        Map<String, Order> sortMap = new HashMap<>(1);
        sortMap.put("id",Order.ASCENDING);
        mySqlPagingQueryProvider.setSortKeys(sortMap);
        reader.setQueryProvider(mySqlPagingQueryProvider);
        return reader;
    }
}
