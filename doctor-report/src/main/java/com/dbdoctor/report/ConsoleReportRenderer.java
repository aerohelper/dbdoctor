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
import com.dbdoctor.core.model.Severity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Renders a scan report as a human-readable console report. */
public class ConsoleReportRenderer implements ReportRenderer {

    private static final Map<String, String> CATEGORY_BY_PREFIX = Map.of(
            "CLUSTER", "Clusters",
            "JOB", "Jobs",
            "SQL", "SQL Warehouses"
    );

    @Override
    public String render(ScanReport report, HealthScore score) {
        StringBuilder sb = new StringBuilder();

        sb.append("╔════════════════════════════════════╗\n");
        sb.append("║          DATABRICKS DOCTOR          ║\n");
        sb.append("╚════════════════════════════════════╝\n\n");

        sb.append("Workspace: ").append(report.workspaceUrl()).append('\n');
        sb.append("Workspace Health: ").append(score.score()).append("/100\n\n");

        Map<String, List<CheckResult>> byCategory = new LinkedHashMap<>();
        for (CheckResult result : report.results()) {
            String category = categoryOf(result.checkId());
            byCategory.computeIfAbsent(category, k -> new java.util.ArrayList<>()).add(result);
        }

        for (Map.Entry<String, List<CheckResult>> entry : byCategory.entrySet()) {
            sb.append(entry.getKey()).append('\n');
            sb.append("─".repeat(entry.getKey().length())).append('\n');
            appendCountLine(sb, "✓", entry.getValue(), Severity.PASS, "passed");
            appendCountLine(sb, "ℹ", entry.getValue(), Severity.INFO, "info");
            appendCountLine(sb, "⚠", entry.getValue(), Severity.WARNING, "warnings");
            appendCountLine(sb, "✗", entry.getValue(), Severity.CRITICAL, "critical");
            sb.append('\n');
        }

        List<CheckResult> issues = report.results().stream()
                .filter(r -> r.severity() != Severity.PASS)
                .sorted((a, b) -> severityRank(a.severity()) - severityRank(b.severity()))
                .collect(Collectors.toList());

        if (!issues.isEmpty()) {
            sb.append("Top Issues\n");
            sb.append("─".repeat(10)).append("\n");
            for (CheckResult issue : issues) {
                sb.append(icon(issue.severity())).append(' ').append(issue.checkId()).append('\n');
                sb.append("  ").append(issue.title()).append('\n');
            }
        }

        return sb.toString();
    }

    private static void appendCountLine(StringBuilder sb, String icon, List<CheckResult> results,
                                         Severity severity, String label) {
        long count = results.stream().filter(r -> r.severity() == severity).count();
        if (count > 0) {
            sb.append(icon).append(' ').append(count).append(' ').append(label).append('\n');
        }
    }

    private static String categoryOf(String checkId) {
        int dash = checkId.indexOf('-');
        String prefix = dash == -1 ? checkId : checkId.substring(0, dash);
        return CATEGORY_BY_PREFIX.getOrDefault(prefix, prefix);
    }

    private static int severityRank(Severity severity) {
        return switch (severity) {
            case CRITICAL -> 0;
            case WARNING -> 1;
            case INFO -> 2;
            case PASS -> 3;
        };
    }

    private static String icon(Severity severity) {
        return switch (severity) {
            case CRITICAL -> "✗";
            case WARNING -> "⚠";
            case INFO -> "ℹ";
            case PASS -> "✓";
        };
    }
}
