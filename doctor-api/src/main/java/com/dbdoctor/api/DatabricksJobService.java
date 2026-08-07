package com.dbdoctor.api;

import com.databricks.sdk.WorkspaceClient;
import com.databricks.sdk.service.jobs.BaseJob;
import com.databricks.sdk.service.jobs.ListJobsRequest;

import java.util.ArrayList;
import java.util.List;

/** {@link JobService} backed by the Databricks SDK. */
public class DatabricksJobService implements JobService {

    private final WorkspaceClient workspaceClient;

    public DatabricksJobService(WorkspaceClient workspaceClient) {
        this.workspaceClient = workspaceClient;
    }

    @Override
    public List<BaseJob> getJobs() {
        List<BaseJob> jobs = new ArrayList<>();
        workspaceClient.jobs().list(new ListJobsRequest()).forEach(jobs::add);
        return jobs;
    }
}
