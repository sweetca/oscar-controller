package com.oscar.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduleJobService {

    private final JobService jobService;

    @Autowired
    public ScheduleJobService(JobService jobService) {
        this.jobService = jobService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void dailyJob() {
        this.jobService.initNvdUpdateJob();
    }
}
