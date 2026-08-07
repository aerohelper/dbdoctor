package com.dbdoctor.checks.cluster;

import com.dbdoctor.checks.HealthCheck;
import com.dbdoctor.core.model.CheckResult;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CLUSTER-005 — flags fixed-size clusters with a worker count above a configurable threshold.
 * This is deliberately a simple size cap, not an attempt to compute "optimal" sizing —
 * autoscaling clusters (which report no fixed {@code numWorkers}) are not flagged.
 */
public class OversizedClusterCheck implements HealthCheck {

    private static final int DEFAULT_MAX_WORKERS = 10;

    private final int maxWorkers;

    public OversizedClusterCheck() {
        this(DEFAULT_MAX_WORKERS);
    }

    public OversizedClusterCheck(int maxWorkers) {
        this.maxWorkers = maxWorkers;
    }

    @Override
    public CheckResult execute(WorkspaceSnapshot workspace) {
        List<String> offenders = workspace.clusters().stream()
                .filter(c -> c.numWorkers() != null && c.numWorkers() > maxWorkers)
                .map(c -> c.name() + " (" + c.numWorkers() + " workers)")
                .collect(Collectors.toList());

        if (offenders.isEmpty()) {
            return new CheckResult("CLUSTER-005", "No oversized clusters", Severity.PASS,
                    "No fixed-size cluster exceeds " + maxWorkers + " workers.", "N/A");
        }

        return new CheckResult("CLUSTER-005", "Oversized cluster", Severity.WARNING,
                "These clusters have more than " + maxWorkers + " workers: " + String.join(", ", offenders) + ".",
                "Review whether these clusters need this many workers, or consider autoscaling.");
    }
}
