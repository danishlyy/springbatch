package com.example.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class NestedJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private Job ChildJobOneJob;
    @Autowired
    private Job ChildJobTwoJob;
    @Autowired
    private JobLauncher jobLauncher;

    @Bean
    public Job parentJob(JobRepository repository, PlatformTransactionManager transactionManager){
        return jobBuilderFactory.get("parentJob")
                .start(childJob1(repository,transactionManager))
                .next(childJob2(repository,transactionManager))
                .build();
    }

    private Step childJob2(JobRepository repository, PlatformTransactionManager transactionManager) {
        return new JobStepBuilder(new StepBuilder("childJob2"))
                .job(ChildJobTwoJob)
                .launcher(jobLauncher)
                .repository(repository)
                .transactionManager(transactionManager)
                .build();
    }

    private Step childJob1(JobRepository repository, PlatformTransactionManager transactionManager) {
        return new JobStepBuilder(new StepBuilder("childJob2"))
                .job(ChildJobOneJob)
                .launcher(jobLauncher)
                .repository(repository)
                .transactionManager(transactionManager)
                .build();
    }
}
