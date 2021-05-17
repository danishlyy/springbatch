package com.example.springbatch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomSkipListener implements SkipListener<String,String> {

    @Override
    public void onSkipInRead(Throwable t) {

    }

    @Override
    public void onSkipInWrite(String item, Throwable t) {

    }

    @Override
    public void onSkipInProcess(String item, Throwable t) {
        log.info("item:{} occur exception:{}",item,t);
    }
}
