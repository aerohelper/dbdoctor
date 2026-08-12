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
 * JOB-005 — flags jobs with no email or webhook notification configured for run failure.
 * Without one, a failing job can go unnoticed until someone happens to check the job list.
 */
public class MissingFailureNotificationsCheck implements HealthCheck {

    @Override
    public CheckResult execute(WorkspaceSnapshot workspace) {
        List<String> offenders = workspace.jobs().stream()
                .filter(j -> !j.hasFailureNotifications())
                .map(j -> j.name())
                .collect(Collectors.toList());

        if (offenders.isEmpty()) {
            return new CheckResult("JOB-005", "Failure notifications are configured", Severity.PASS,
                    "Every job has an email or webhook notification configured for failure.", "N/A");
        }

        return new CheckResult("JOB-005", "Missing failure notifications", Severity.WARNING,
                "These jobs have no email or webhook notification configured for failure: "
                        + String.join(", ", offenders) + ".",
                "Configure a failure notification so job failures don't go unnoticed.");
    }
}
