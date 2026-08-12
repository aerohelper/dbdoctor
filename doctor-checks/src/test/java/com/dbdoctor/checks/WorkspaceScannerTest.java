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

package com.dbdoctor.checks;

import com.dbdoctor.api.ClusterService;
import com.dbdoctor.api.JobService;
import com.dbdoctor.api.WarehouseService;
import com.dbdoctor.core.model.CheckResult;
import com.dbdoctor.core.model.ScanReport;
import com.dbdoctor.core.model.Severity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WorkspaceScannerTest {

    @Test
    void collectsSnapshotOnceAndRunsEveryCheckAgainstIt() {
        ClusterService clusters = List::of;
        JobService jobs = List::of;
        WarehouseService warehouses = List::of;

        HealthCheck alwaysPass = workspace -> new CheckResult("X-1", "ok", Severity.PASS, "d", "r");
        HealthCheck alwaysWarn = workspace -> new CheckResult("X-2", "not ok", Severity.WARNING, "d", "r");

        WorkspaceScanner scanner = new WorkspaceScanner(
                "https://example.cloud.databricks.com", clusters, jobs, warehouses,
                List.of(alwaysPass, alwaysWarn));

        ScanReport report = scanner.scan();

        assertEquals("https://example.cloud.databricks.com", report.workspaceUrl());
        assertEquals(2, report.results().size());
        assertEquals(Severity.PASS, report.results().get(0).severity());
        assertEquals(Severity.WARNING, report.results().get(1).severity());
    }
}
