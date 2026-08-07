package com.dbdoctor.checks.cluster;

import com.dbdoctor.checks.HealthCheck;
import com.dbdoctor.core.model.CheckResult;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CLUSTER-006 — flags clusters with local disk encryption disabled (or unreported). Data
 * spilled to local disk during processing is not encrypted at rest unless this is enabled.
 */
public class LocalDiskEncryptionDisabledCheck implements HealthCheck {

    @Override
    public CheckResult execute(WorkspaceSnapshot workspace) {
        List<String> offenders = workspace.clusters().stream()
                .filter(c -> !Boolean.TRUE.equals(c.localDiskEncryptionEnabled()))
                .map(c -> c.name())
                .collect(Collectors.toList());

        if (offenders.isEmpty()) {
            return new CheckResult("CLUSTER-006", "Local disk encryption is enabled", Severity.PASS,
                    "All clusters have local disk encryption enabled.", "N/A");
        }

        return new CheckResult("CLUSTER-006", "Local disk encryption disabled", Severity.WARNING,
                "These clusters do not have local disk encryption enabled: " + String.join(", ", offenders) + ".",
                "Enable local disk encryption on these clusters if they process sensitive data.");
    }
}
