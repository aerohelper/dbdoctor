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
