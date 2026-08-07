package com.dbdoctor.checks.cluster;

import com.dbdoctor.checks.HealthCheck;
import com.dbdoctor.core.model.CheckResult;
import com.dbdoctor.core.model.ClusterInfo;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CLUSTER-001 — flags clusters with auto-termination disabled ({@code autoTerminationMinutes}
 * is {@code 0} or unset). Idle clusters that never auto-terminate accrue unnecessary cost.
 */
public class AutoTerminationCheck implements HealthCheck {

    @Override
    public CheckResult execute(WorkspaceSnapshot workspace) {
        List<String> offenders = workspace.clusters().stream()
                .filter(c -> c.autoTerminationMinutes() == null || c.autoTerminationMinutes() == 0)
                .map(ClusterInfo::name)
                .collect(Collectors.toList());

        if (offenders.isEmpty()) {
            return new CheckResult("CLUSTER-001", "Auto termination is configured", Severity.PASS,
                    "All clusters have auto termination enabled.", "N/A");
        }

        return new CheckResult("CLUSTER-001", "Auto termination is disabled", Severity.CRITICAL,
                "Auto termination is disabled on: " + String.join(", ", offenders)
                        + ". Idle clusters will keep running (and billing) indefinitely.",
                "Configure automatic termination on these clusters.");
    }
}
