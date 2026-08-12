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
