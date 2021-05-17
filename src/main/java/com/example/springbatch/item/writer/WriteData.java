package com.example.springbatch.item.writer;

import com.example.springbatch.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;


import java.util.List;

@Slf4j
@Component
public class WriteData implements ItemWriter<User> {
    @Override
    public void write(List<? extends User> items) throws Exception {
        items.forEach(p-> log.info("输出信息:{}",p));
    }
}
