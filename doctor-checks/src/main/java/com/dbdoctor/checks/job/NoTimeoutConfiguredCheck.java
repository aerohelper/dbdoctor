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
 * JOB-004 — flags jobs with no run timeout configured. Without a timeout, a stuck run can
 * occupy a cluster (and accrue cost) indefinitely instead of failing and freeing resources.
 */
public class NoTimeoutConfiguredCheck implements HealthCheck {

    @Override
    public CheckResult execute(WorkspaceSnapshot workspace) {
        List<String> offenders = workspace.jobs().stream()
                .filter(j -> j.timeoutSeconds() == null || j.timeoutSeconds() == 0)
                .map(j -> j.name())
                .collect(Collectors.toList());

        if (offenders.isEmpty()) {
            return new CheckResult("JOB-004", "Timeouts are configured", Severity.PASS,
                    "Every job has a run timeout configured.", "N/A");
        }

        return new CheckResult("JOB-004", "No timeout configured", Severity.WARNING,
                "These jobs have no run timeout configured: " + String.join(", ", offenders) + ".",
                "Configure a run timeout so a stuck run doesn't occupy compute indefinitely.");
    }
}
