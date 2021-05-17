package com.example.springbatch.database;

import com.example.springbatch.item.writer.WriteData;
import com.example.springbatch.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
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

@Slf4j
@Configuration
public class ItemReaderDbJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private WriteData writeData;

    @Bean
    public Job itemReaderDataBaseJob(){
        return jobBuilderFactory.get("itemReaderDataBaseJob")
                .start(itemReaderDataFromDb())
                .build();
    }

    @Bean
    public Step itemReaderDataFromDb() {
        return stepBuilderFactory.get("itemReaderDataFromDb")
                .<User,User>chunk(2)
                .reader(readUser())
                .writer(writeData)
                .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<User> readUser() {
        JdbcPagingItemReader<User> itemReader = new JdbcPagingItemReader<>();
        itemReader.setDataSource(dataSource);
        itemReader.setFetchSize(2);
        itemReader.setRowMapper((resultSet, rowNumber) -> {
            User user = new User();
            user.setId(resultSet.getInt(1));
            user.setUserName(resultSet.getString(2));
            user.setPassword(resultSet.getString(3));
            user.setAge(resultSet.getInt(4));
            return user;
        });

        MySqlPagingQueryProvider mySqlPagingQueryProvider = new MySqlPagingQueryProvider();
        mySqlPagingQueryProvider.setSelectClause("id,username,password,age");
        mySqlPagingQueryProvider.setFromClause("from user");
        Map<String, Order> sortMap = new HashMap<>();
        sortMap.put("id",Order.DESCENDING);
        mySqlPagingQueryProvider.setSortKeys(sortMap);
        itemReader.setQueryProvider(mySqlPagingQueryProvider);
        return itemReader;
    }

}
