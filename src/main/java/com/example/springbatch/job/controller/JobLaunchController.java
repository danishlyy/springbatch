package com.example.springbatch.job.controller;



import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobLaunchController {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job jobLaunch;

    @Autowired
    private JobOperator jobOperator;

    @GetMapping(value = "/test/run/{msg}")
    public String launchJob(@PathVariable("msg") String msg) throws JobParametersInvalidException,
            JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters parameter = new JobParametersBuilder()
                .addString("msg",msg)
                .toJobParameters();
        jobLauncher.run(jobLaunch,parameter);
        return "job success";
    }

    @GetMapping(value = "/test/run/jobOperate/{msg}")
    public String jobOperateRun(@PathVariable("msg") String msg) throws JobParametersInvalidException, JobInstanceAlreadyExistsException, NoSuchJobException {
        jobOperator.start("jobOperatorDemoJob","msg="+msg);
        return "job success";
    }
}
