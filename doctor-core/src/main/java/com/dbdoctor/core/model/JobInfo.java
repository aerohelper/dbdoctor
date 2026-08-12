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
