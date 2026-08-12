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

package com.dbdoctor.report;

import com.dbdoctor.core.model.CheckResult;
import com.dbdoctor.core.model.ScanReport;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;
import com.dbdoctor.core.score.ScoreCalculator;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsoleReportRendererTest {

    private final ConsoleReportRenderer renderer = new ConsoleReportRenderer();
    private final ScoreCalculator scoreCalculator = new ScoreCalculator();

    @Test
    void rendersScoreAndCategoriesAndIssues() {
        List<CheckResult> results = List.of(
                new CheckResult("CLUSTER-001", "Auto termination disabled", Severity.CRITICAL, "desc", "rec"),
                new CheckResult("CLUSTER-002", "Auto termination periods are reasonable", Severity.PASS, "desc", "rec"),
                new CheckResult("JOB-001", "Retry policy not configured", Severity.WARNING, "desc", "rec"),
                new CheckResult("SQL-001", "Auto-stop is configured", Severity.PASS, "desc", "rec")
        );
        ScanReport report = new ScanReport("https://example.cloud.databricks.com", Instant.now(),
                new WorkspaceSnapshot(List.of(), List.of(), List.of()), results);

        String output = renderer.render(report, scoreCalculator.calculate(results));

        assertTrue(output.contains("DATABRICKS DOCTOR"));
        assertTrue(output.contains("Workspace Health: 80/100"));
        assertTrue(output.contains("Clusters"));
        assertTrue(output.contains("Jobs"));
        assertTrue(output.contains("SQL Warehouses"));
        assertTrue(output.contains("Top Issues"));
        assertTrue(output.contains("CLUSTER-001"));
        assertTrue(output.contains("JOB-001"));
    }

    @Test
    void noIssuesMeansNoTopIssuesSection() {
        List<CheckResult> results = List.of(
                new CheckResult("SQL-001", "Auto-stop is configured", Severity.PASS, "desc", "rec")
        );
        ScanReport report = new ScanReport("https://example.cloud.databricks.com", Instant.now(),
                new WorkspaceSnapshot(List.of(), List.of(), List.of()), results);

        String output = renderer.render(report, scoreCalculator.calculate(results));

        assertTrue(output.contains("Workspace Health: 100/100"));
        assertTrue(!output.contains("Top Issues"));
    }
}
