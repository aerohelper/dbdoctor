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

package com.dbdoctor.checks.job;

import com.dbdoctor.checks.HealthCheck;
import com.dbdoctor.core.model.CheckResult;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;

import java.util.List;
import java.util.stream.Collectors;

/**
 * JOB-001 — flags jobs where no task has a retry policy configured. A transient failure
 * (a brief cluster blip, a flaky upstream dependency) will fail the whole job run outright.
 *
 * <p>Jobs whose task list dbdoctor couldn't evaluate ({@code hasConfiguredRetries == null})
 * are skipped rather than flagged, since we can't tell either way.
 */
public class NoRetryConfigurationCheck implements HealthCheck {

    @Override
    public CheckResult execute(WorkspaceSnapshot workspace) {
        List<String> offenders = workspace.jobs().stream()
                .filter(j -> Boolean.FALSE.equals(j.hasConfiguredRetries()))
                .map(j -> j.name())
                .collect(Collectors.toList());

        if (offenders.isEmpty()) {
            return new CheckResult("JOB-001", "Retry policies are configured", Severity.PASS,
                    "No job is missing a retry policy on all of its tasks.", "N/A");
        }

        return new CheckResult("JOB-001", "Retry policy not configured", Severity.WARNING,
                "These jobs have no retry policy on any task: " + String.join(", ", offenders) + ".",
                "Configure task-level retries so transient failures don't fail the whole job run.");
    }
}
