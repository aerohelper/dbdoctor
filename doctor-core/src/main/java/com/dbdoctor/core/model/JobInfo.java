package com.dbdoctor.core.model;

import java.util.Map;

/**
 * dbdoctor's own representation of a Databricks job, decoupled from the SDK's model.
 *
 * @param id                 job ID
 * @param name               job name
 * @param timeoutSeconds     configured run timeout; {@code null} means no timeout is set
 * @param maxConcurrentRuns  configured maximum concurrent runs
 * @param tags               job tags, as configured in the workspace
 */
public record JobInfo(
        Long id,
        String name,
        Long timeoutSeconds,
        Long maxConcurrentRuns,
        Map<String, String> tags
) {
}
