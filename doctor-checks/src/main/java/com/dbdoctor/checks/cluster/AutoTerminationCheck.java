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
