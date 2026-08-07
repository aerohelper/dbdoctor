package com.dbdoctor.checks.cluster;

import com.dbdoctor.checks.HealthCheck;
import com.dbdoctor.core.model.CheckResult;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CLUSTER-004 — flags clusters not governed by a cluster policy, for workspaces where policies
 * are expected to be used everywhere (e.g. for cost control or configuration standardization).
 */
public class MissingClusterPolicyCheck implements HealthCheck {

    @Override
    public CheckResult execute(WorkspaceSnapshot workspace) {
        List<String> offenders = workspace.clusters().stream()
                .filter(c -> c.policyId() == null || c.policyId().isBlank())
                .map(c -> c.name())
                .collect(Collectors.toList());

        if (offenders.isEmpty()) {
            return new CheckResult("CLUSTER-004", "All clusters are governed by a policy", Severity.PASS,
                    "Every cluster has a cluster policy attached.", "N/A");
        }

        return new CheckResult("CLUSTER-004", "Cluster policy missing", Severity.WARNING,
                "These clusters have no cluster policy attached: " + String.join(", ", offenders) + ".",
                "Attach a cluster policy to standardize configuration and control costs.");
    }
}
