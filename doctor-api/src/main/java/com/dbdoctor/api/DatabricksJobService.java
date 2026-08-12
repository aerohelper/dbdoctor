/*
 * Copyright 2026 Databricks Doctor contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dbdoctor.api;

import com.databricks.sdk.WorkspaceClient;
import com.databricks.sdk.service.jobs.BaseJob;
import com.databricks.sdk.service.jobs.BaseRun;
import com.databricks.sdk.service.jobs.JobSettings;
import com.databricks.sdk.service.jobs.ListJobsRequest;
import com.databricks.sdk.service.jobs.ListRunsRequest;
import com.databricks.sdk.service.jobs.RunResultState;
import com.databricks.sdk.service.jobs.JobEmailNotifications;
import com.databricks.sdk.service.jobs.Task;
import com.databricks.sdk.service.jobs.WebhookNotifications;
import com.dbdoctor.core.model.JobInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** {@link JobService} backed by the Databricks SDK. */
public class DatabricksJobService implements JobService {

    /** How many of the most recent runs to inspect per job for failure/duration checks. */
    private static final long RECENT_RUNS_LIMIT = 10;

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

    private JobInfo toJobInfo(BaseJob job) {
        JobSettings settings = job.getSettings();
        String name = settings == null ? null : settings.getName();
        Long timeoutSeconds = settings == null ? null : settings.getTimeoutSeconds();
        Long maxConcurrentRuns = settings == null ? null : settings.getMaxConcurrentRuns();
        Map<String, String> tags = settings == null ? null : settings.getTags();
        Boolean hasConfiguredRetries = hasConfiguredRetries(settings);

        List<BaseRun> recentRuns = fetchRecentRuns(job.getJobId());
        Integer recentFailureCount = recentRuns == null ? null : countFailures(recentRuns);
        Long lastRunDurationMinutes = recentRuns == null ? null : lastRunDurationMinutes(recentRuns);

        return new JobInfo(job.getJobId(), name, timeoutSeconds, maxConcurrentRuns, tags,
                hasConfiguredRetries, recentFailureCount, lastRunDurationMinutes,
                hasFailureNotifications(settings));
    }

    private static boolean hasFailureNotifications(JobSettings settings) {
        if (settings == null) {
            return false;
        }
        JobEmailNotifications email = settings.getEmailNotifications();
        if (email != null && email.getOnFailure() != null && !email.getOnFailure().isEmpty()) {
            return true;
        }
        WebhookNotifications webhooks = settings.getWebhookNotifications();
        return webhooks != null && webhooks.getOnFailure() != null && !webhooks.getOnFailure().isEmpty();
    }

    private static Boolean hasConfiguredRetries(JobSettings settings) {
        if (settings == null || settings.getTasks() == null || settings.getTasks().isEmpty()) {
            return null;
        }
        for (Task task : settings.getTasks()) {
            Long maxRetries = task.getMaxRetries();
            if (maxRetries != null && maxRetries > 0) {
                return true;
            }
        }
        return false;
    }

    private List<BaseRun> fetchRecentRuns(Long jobId) {
        if (jobId == null) {
            return null;
        }
        try {
            List<BaseRun> runs = new ArrayList<>();
            ListRunsRequest request = new ListRunsRequest().setJobId(jobId).setLimit(RECENT_RUNS_LIMIT);
            workspaceClient.jobs().listRuns(request).forEach(runs::add);
            return runs;
        } catch (Exception e) {
            // Run history may be unavailable (permissions, job never ran, etc.) — degrade gracefully.
            return null;
        }
    }

    private static int countFailures(List<BaseRun> runs) {
        int failures = 0;
        for (BaseRun run : runs) {
            if (run.getState() != null && run.getState().getResultState() == RunResultState.FAILED) {
                failures++;
            }
        }
        return failures;
    }

    private static Long lastRunDurationMinutes(List<BaseRun> runs) {
        return runs.stream()
                .filter(r -> r.getRunDuration() != null)
                .max((a, b) -> Long.compare(nvl(a.getStartTime()), nvl(b.getStartTime())))
                .map(r -> r.getRunDuration() / 60000)
                .orElse(null);
    }

    private static long nvl(Long value) {
        return value == null ? 0L : value;
    }
}
