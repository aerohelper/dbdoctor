package com.dbdoctor.report;

import com.dbdoctor.core.model.CheckResult;
import com.dbdoctor.core.model.HealthScore;
import com.dbdoctor.core.model.ScanReport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.Instant;
import java.util.List;

/**
 * Renders a scan report as JSON, suitable for CI/CD consumption (e.g. failing a build
 * if critical issues exist).
 */
public class JsonReportRenderer implements ReportRenderer {

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .enable(SerializationFeature.INDENT_OUTPUT)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public String render(ScanReport report, HealthScore score) {
        JsonReport json = new JsonReport(
                report.workspaceUrl(),
                report.scannedAt(),
                score.score(),
                new Summary(score.total(), score.passed(), score.info(), score.warnings(), score.criticals()),
                report.results()
        );
        try {
            return mapper.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to render scan report as JSON", e);
        }
    }

    private record JsonReport(
            String workspace,
            Instant scannedAt,
            int score,
            Summary summary,
            List<CheckResult> checks
    ) {
    }

    private record Summary(int total, int passed, int info, int warning, int critical) {
    }
}
