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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonReportRendererTest {

    private final JsonReportRenderer renderer = new JsonReportRenderer();
    private final ScoreCalculator scoreCalculator = new ScoreCalculator();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void producesValidParseableJsonWithExpectedShape() throws Exception {
        List<CheckResult> results = List.of(
                new CheckResult("CLUSTER-001", "Auto termination disabled", Severity.CRITICAL, "desc", "rec"),
                new CheckResult("SQL-001", "Auto-stop is configured", Severity.PASS, "desc", "rec")
        );
        ScanReport report = new ScanReport("https://example.cloud.databricks.com", Instant.now(),
                new WorkspaceSnapshot(List.of(), List.of(), List.of()), results);

        String json = renderer.render(report, scoreCalculator.calculate(results));
        JsonNode node = mapper.readTree(json);

        assertEquals(85, node.get("score").asInt());
        assertEquals(1, node.get("summary").get("critical").asInt());
        assertEquals(1, node.get("summary").get("passed").asInt());
        assertEquals(2, node.get("checks").size());
        assertTrue(node.get("checks").get(0).get("checkId").asText().equals("CLUSTER-001"));
    }

    @Test
    void serializesScannedAtAsIsoStringNotEpochTimestamp() throws Exception {
        ScanReport report = new ScanReport("https://example.cloud.databricks.com", Instant.now(),
                new WorkspaceSnapshot(List.of(), List.of(), List.of()), List.of());

        String json = renderer.render(report, scoreCalculator.calculate(List.of()));
        JsonNode node = mapper.readTree(json);

        assertTrue(node.get("scannedAt").isTextual(), "scannedAt should be an ISO-8601 string, not a numeric timestamp");
    }
}
