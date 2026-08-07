package com.dbdoctor.checks.cluster;

import com.dbdoctor.checks.HealthCheck;
import com.dbdoctor.core.model.CheckResult;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CLUSTER-002 — flags clusters whose auto-termination timeout is configured but very high,
 * which reduces the practical benefit of having it enabled at all.
 *
 * <p>The default threshold (120 minutes) is dbdoctor's own opinion, not a documented Databricks
 * best practice — pass a different value to the constructor to change it.
 */
public class ExcessiveAutoTerminationCheck implements HealthCheck {

    private static final int DEFAULT_MAX_MINUTES = 120;

    private final int maxAutoTerminationMinutes;

    public ExcessiveAutoTerminationCheck() {
        this(DEFAULT_MAX_MINUTES);
    }

    public ExcessiveAutoTerminationCheck(int maxAutoTerminationMinutes) {
        this.maxAutoTerminationMinutes = maxAutoTerminationMinutes;
    }

    @Override
    public CheckResult execute(WorkspaceSnapshot workspace) {
        List<String> offenders = workspace.clusters().stream()
                .filter(c -> c.autoTerminationMinutes() != null && c.autoTerminationMinutes() > maxAutoTerminationMinutes)
                .map(c -> c.name() + " (" + c.autoTerminationMinutes() + " min)")
                .collect(Collectors.toList());

        if (offenders.isEmpty()) {
            return new CheckResult("CLUSTER-002", "Auto termination periods are reasonable", Severity.PASS,
                    "No cluster exceeds the " + maxAutoTerminationMinutes + "-minute auto termination threshold.", "N/A");
        }

        return new CheckResult("CLUSTER-002", "Auto termination period is excessive", Severity.WARNING,
                "These clusters have an auto termination timeout above " + maxAutoTerminationMinutes
                        + " minutes: " + String.join(", ", offenders) + ".",
                "Consider lowering the auto termination timeout to reduce idle cost exposure.");
    }
}
