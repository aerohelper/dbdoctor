package com.dbdoctor.checks.job;

import com.dbdoctor.checks.HealthCheck;
import com.dbdoctor.core.model.CheckResult;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;

import java.util.List;
import java.util.stream.Collectors;

/**
 * JOB-002 — flags jobs with a high failure count among their most recently collected runs.
 * A job that repeatedly fails often signals a broken pipeline rather than transient issues.
 *
 * <p>Jobs whose run history dbdoctor couldn't fetch ({@code recentFailureCount == null}) are
 * skipped rather than flagged.
 */
public class RepeatedFailuresCheck implements HealthCheck {

    private static final int DEFAULT_FAILURE_THRESHOLD = 3;

    private final int failureThreshold;

    public RepeatedFailuresCheck() {
        this(DEFAULT_FAILURE_THRESHOLD);
    }

    public RepeatedFailuresCheck(int failureThreshold) {
        this.failureThreshold = failureThreshold;
    }

    @Override
    public CheckResult execute(WorkspaceSnapshot workspace) {
        List<String> offenders = workspace.jobs().stream()
                .filter(j -> j.recentFailureCount() != null && j.recentFailureCount() >= failureThreshold)
                .map(j -> j.name() + " (" + j.recentFailureCount() + " recent failures)")
                .collect(Collectors.toList());

        if (offenders.isEmpty()) {
            return new CheckResult("JOB-002", "No jobs are repeatedly failing", Severity.PASS,
                    "No job has " + failureThreshold + " or more failures among its recent runs.", "N/A");
        }

        return new CheckResult("JOB-002", "Job is repeatedly failing", Severity.CRITICAL,
                "These jobs have " + failureThreshold + " or more failures among their recent runs: "
                        + String.join(", ", offenders) + ".",
                "Investigate and fix the underlying cause of these failures.");
    }
}
