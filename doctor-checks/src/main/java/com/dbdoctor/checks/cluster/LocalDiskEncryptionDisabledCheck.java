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
