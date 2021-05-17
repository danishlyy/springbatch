package com.example.springbatch.retry;

import com.example.springbatch.exception.RetryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RetryProcessor implements ItemProcessor<String,String> {

    private int retryTimes = 0;

    @Override
    public String process(String item) throws Exception {
        log.info("retry processor: {}",item);
        if ("30".equals(item)){
            retryTimes++;
            if (retryTimes > 3){
                log.info("this has retry:{} times success",retryTimes);
                return String.valueOf(Integer.valueOf(item) * -1);
            }else {
                log.info("this has retry:{} times failed",retryTimes);
                throw new RetryException("000000","retry failed");
            }
        }
        return String.valueOf(Integer.valueOf(item) * -1);

    }
}
