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
