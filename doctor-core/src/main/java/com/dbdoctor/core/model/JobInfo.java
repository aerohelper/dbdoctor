package com.dbdoctor.core.model;

import java.util.Map;

/**
 * dbdoctor's own representation of a Databricks job, decoupled from the SDK's model.
 *
 * @param id                       job ID
 * @param name                     job name
 * @param timeoutSeconds           configured run timeout; {@code null} means no timeout is set
 * @param maxConcurrentRuns        configured maximum concurrent runs
 * @param tags                     job tags, as configured in the workspace
 * @param hasConfiguredRetries     whether at least one task on this job has a retry policy configured;
 *                                 {@code null} if the job has no tasks to evaluate
 * @param recentFailureCount       number of failed runs among the most recently collected runs
 * @param lastRunDurationMinutes   duration of the most recent completed run, in minutes; {@code null} if unknown
 * @param hasFailureNotifications  whether the job has at least one email or webhook notification
 *                                 configured for run failure
 */
public record JobInfo(
        Long id,
        String name,
        Long timeoutSeconds,
        Long maxConcurrentRuns,
        Map<String, String> tags,
        Boolean hasConfiguredRetries,
        Integer recentFailureCount,
        Long lastRunDurationMinutes,
        boolean hasFailureNotifications
) {
}
