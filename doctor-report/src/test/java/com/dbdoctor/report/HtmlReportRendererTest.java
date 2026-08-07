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

class HtmlReportRendererTest {

    private final HtmlReportRenderer renderer = new HtmlReportRenderer();
    private final ScoreCalculator scoreCalculator = new ScoreCalculator();

    @Test
    void producesWellFormedStandaloneHtml() {
        List<CheckResult> results = List.of(
                new CheckResult("CLUSTER-001", "Auto termination disabled", Severity.CRITICAL, "desc", "rec"),
                new CheckResult("SQL-001", "Auto-stop is configured", Severity.PASS, "desc", "rec")
        );
        ScanReport report = new ScanReport("https://example.cloud.databricks.com", Instant.now(),
                new WorkspaceSnapshot(List.of(), List.of(), List.of()), results);

        String html = renderer.render(report, scoreCalculator.calculate(results));

        assertTrue(html.startsWith("<!doctype html>"));
        assertTrue(html.contains("Databricks Doctor"));
        assertTrue(html.contains("85 / 100"));
        assertTrue(html.contains("CLUSTER-001"));
        assertTrue(html.contains("Clusters"));
        assertTrue(html.contains("SQL Warehouses"));
        assertTrue(html.endsWith("</html>"));
    }

    @Test
    void escapesHtmlSpecialCharactersInContent() {
        List<CheckResult> results = List.of(
                new CheckResult("JOB-001", "<script>alert(1)</script>", Severity.WARNING, "a & b", "rec")
        );
        ScanReport report = new ScanReport("https://example.cloud.databricks.com", Instant.now(),
                new WorkspaceSnapshot(List.of(), List.of(), List.of()), results);

        String html = renderer.render(report, scoreCalculator.calculate(results));

        assertTrue(!html.contains("<script>alert(1)</script>"));
        assertTrue(html.contains("&lt;script&gt;"));
        assertTrue(html.contains("a &amp; b"));
    }
}
