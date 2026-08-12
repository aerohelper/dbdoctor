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

package com.dbdoctor.checks.warehouse;

import com.dbdoctor.checks.HealthCheck;
import com.dbdoctor.core.model.CheckResult;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;

import java.util.List;
import java.util.stream.Collectors;

/**
 * SQL-001 — flags SQL warehouses with auto-stop disabled ({@code auto_stop_mins} is
 * {@code 0} or unset). An idle warehouse that never stops keeps accruing DBU cost.
 */
public class AutoStopDisabledCheck implements HealthCheck {

    @Override
    public CheckResult execute(WorkspaceSnapshot workspace) {
        List<String> offenders = workspace.warehouses().stream()
                .filter(w -> w.autoStopMinutes() == null || w.autoStopMinutes() == 0)
                .map(w -> w.name())
                .collect(Collectors.toList());

        if (offenders.isEmpty()) {
            return new CheckResult("SQL-001", "Auto-stop is configured", Severity.PASS,
                    "All SQL warehouses have auto-stop enabled.", "N/A");
        }

        return new CheckResult("SQL-001", "Auto-stop is disabled", Severity.CRITICAL,
                "Auto-stop is disabled on: " + String.join(", ", offenders)
                        + ". Idle warehouses will keep running (and billing) indefinitely.",
                "Configure auto-stop on these warehouses.");
    }
}
