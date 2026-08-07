package com.dbdoctor.api;

import com.databricks.sdk.WorkspaceClient;
import com.databricks.sdk.service.jobs.BaseJob;
import com.databricks.sdk.service.jobs.JobSettings;
import com.databricks.sdk.service.jobs.ListJobsRequest;
import com.dbdoctor.core.model.JobInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** {@link JobService} backed by the Databricks SDK. */
public class DatabricksJobService implements JobService {

    private final WorkspaceClient workspaceClient;

    public DatabricksJobService(WorkspaceClient workspaceClient) {
        this.workspaceClient = workspaceClient;
    }

    @Override
    public List<JobInfo> getJobs() {
        List<JobInfo> jobs = new ArrayList<>();
        workspaceClient.jobs().list(new ListJobsRequest())
                .forEach(job -> jobs.add(toJobInfo(job)));
        return jobs;
    }

    private static JobInfo toJobInfo(BaseJob job) {
        JobSettings settings = job.getSettings();
        String name = settings == null ? null : settings.getName();
        Long timeoutSeconds = settings == null ? null : settings.getTimeoutSeconds();
        Long maxConcurrentRuns = settings == null ? null : settings.getMaxConcurrentRuns();
        Map<String, String> tags = settings == null ? null : settings.getTags();
        return new JobInfo(job.getJobId(), name, timeoutSeconds, maxConcurrentRuns, tags);
    }
}
