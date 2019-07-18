package com.oscar.controller.service;

import com.oscar.controller.model.job.Job;
import com.oscar.controller.model.job.JobType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ScheduleJobService {

    private final String podName = "oscar-controller-1";
    private final JobService jobService;
    private final ComponentService componentService;

    @Autowired
    public ScheduleJobService(JobService jobService,
                              ComponentService componentService) {
        this.jobService = jobService;
        this.componentService = componentService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void dailyJob() {
        this.jobService.initNvdUpdateJob();
    }

    private boolean vulnerabilityJob = false;
    @Scheduled(cron = "0 */1 * * * *")
    public void proceedVulnerabilityJob() {
        if (vulnerabilityJob) {
            return;
        }
        vulnerabilityJob = true;
        try {
            Job job = this.jobService.findJobForProcessing(JobType.vulnerabilities.getType(), podName);
            if (job != null) {
                log.info("vulnerabilities job started {}", job.getId());
                boolean result = this.componentService
                        .proceedComponentVulnerabilities(job.getComponent(), job.getVersion()).get();
                if (result) {
                    log.info("Component vulnerabilities data created {}/{}", job.getComponent(), job.getVersion());
                }
                this.jobService.finishJob(job.getId(), podName);
                log.info("vulnerabilities job finished {}", job.getId());
            }
        } catch (Exception e) {
            log.error("Error proceedVulnerabilityJob", e);
        } finally {
            vulnerabilityJob = false;
        }
    }
}
