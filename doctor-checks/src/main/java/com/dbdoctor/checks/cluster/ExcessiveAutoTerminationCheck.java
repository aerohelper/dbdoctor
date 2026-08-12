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
