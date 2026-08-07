package com.dbdoctor.api;

import com.databricks.sdk.service.jobs.BaseJob;

import java.util.List;

/** Abstraction over job access, so health checks don't depend on the Databricks SDK directly. */
public interface JobService {

    /** Returns all jobs visible to the authenticated user in this workspace. */
    List<BaseJob> getJobs();
}
