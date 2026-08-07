package com.dbdoctor.checks.warehouse;

import com.dbdoctor.checks.HealthCheck;
import com.dbdoctor.core.model.CheckResult;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * SQL-002 — flags warehouses at or above a configurable size tier. This is deliberately a
 * simple size-tier cap, not an attempt at intelligent sizing based on actual usage.
 */
public class OversizedWarehouseCheck implements HealthCheck {

    private static final Set<String> DEFAULT_LARGE_SIZES = Set.of(
            "Large", "X-Large", "2X-Large", "3X-Large", "4X-Large");

    private final Set<String> largeSizes;

    public OversizedWarehouseCheck() {
        this(DEFAULT_LARGE_SIZES);
    }

    public OversizedWarehouseCheck(Set<String> largeSizes) {
        this.largeSizes = largeSizes;
    }

    @Override
    public CheckResult execute(WorkspaceSnapshot workspace) {
        List<String> offenders = workspace.warehouses().stream()
                .filter(w -> w.clusterSize() != null && largeSizes.contains(w.clusterSize()))
                .map(w -> w.name() + " (" + w.clusterSize() + ")")
                .collect(Collectors.toList());

        if (offenders.isEmpty()) {
            return new CheckResult("SQL-002", "No oversized warehouses", Severity.PASS,
                    "No warehouse is sized " + largeSizes + " or larger.", "N/A");
        }

        return new CheckResult("SQL-002", "Oversized warehouse", Severity.WARNING,
                "These warehouses are sized Large or bigger: " + String.join(", ", offenders) + ".",
                "Review whether this size is actually needed based on query load.");
    }
}
