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
 * CLUSTER-007 — flags clusters that don't draw instances from a cluster instance pool.
 * Pools reduce cluster start/autoscaling latency and can reduce cloud costs via idle
 * instance reuse. This is an opinionated suggestion, not a hard requirement — many
 * workspaces run fine without pools.
 */
public class NoInstancePoolCheck implements HealthCheck {

    @Override
    public CheckResult execute(WorkspaceSnapshot workspace) {
        List<String> offenders = workspace.clusters().stream()
                .filter(c -> c.instancePoolId() == null || c.instancePoolId().isBlank())
                .map(c -> c.name())
                .collect(Collectors.toList());

        if (offenders.isEmpty()) {
            return new CheckResult("CLUSTER-007", "All clusters use an instance pool", Severity.PASS,
                    "Every cluster draws instances from a pool.", "N/A");
        }

        return new CheckResult("CLUSTER-007", "Cluster not using an instance pool", Severity.INFO,
                "These clusters don't use an instance pool: " + String.join(", ", offenders) + ".",
                "Consider using an instance pool to reduce cluster start time and idle instance cost.");
    }
}
