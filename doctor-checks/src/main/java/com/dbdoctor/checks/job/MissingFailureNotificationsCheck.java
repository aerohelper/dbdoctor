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
