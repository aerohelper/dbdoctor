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
 * JOB-003 — flags jobs whose most recent run took longer than a configurable threshold.
 * Jobs whose run history dbdoctor couldn't fetch ({@code lastRunDurationMinutes == null})
 * are skipped rather than flagged.
 */
public class ExcessiveRuntimeCheck implements HealthCheck {

    private static final long DEFAULT_MAX_RUNTIME_MINUTES = 180;

    private final long maxRuntimeMinutes;

    public ExcessiveRuntimeCheck() {
        this(DEFAULT_MAX_RUNTIME_MINUTES);
    }

    public ExcessiveRuntimeCheck(long maxRuntimeMinutes) {
        this.maxRuntimeMinutes = maxRuntimeMinutes;
    }

    @Override
    public CheckResult execute(WorkspaceSnapshot workspace) {
        List<String> offenders = workspace.jobs().stream()
                .filter(j -> j.lastRunDurationMinutes() != null && j.lastRunDurationMinutes() > maxRuntimeMinutes)
                .map(j -> j.name() + " (" + j.lastRunDurationMinutes() + " min)")
                .collect(Collectors.toList());

        if (offenders.isEmpty()) {
            return new CheckResult("JOB-003", "No jobs are running excessively long", Severity.PASS,
                    "No job's most recent run exceeded " + maxRuntimeMinutes + " minutes.", "N/A");
        }

        return new CheckResult("JOB-003", "Job runtime is excessive", Severity.WARNING,
                "These jobs' most recent run exceeded " + maxRuntimeMinutes + " minutes: "
                        + String.join(", ", offenders) + ".",
                "Investigate why these jobs are taking this long, and consider a lower configured timeout.");
    }
}
