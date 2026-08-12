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

/** Renders a scan report as a standalone HTML page. */
public class HtmlReportRenderer implements ReportRenderer {

    private static final Map<String, String> CATEGORY_BY_PREFIX = Map.of(
            "CLUSTER", "Clusters",
            "JOB", "Jobs",
            "SQL", "SQL Warehouses"
    );

    @Override
    public String render(ScanReport report, HealthScore score) {
        StringBuilder html = new StringBuilder();
        html.append("<!doctype html><html><head><meta charset=\"utf-8\">");
        html.append("<title>Databricks Doctor Report</title>");
        html.append("<style>").append(CSS).append("</style></head><body>");

        html.append("<header><h1>Databricks Doctor</h1>");
        html.append("<p class=\"workspace\">").append(escape(report.workspaceUrl())).append("</p>");
        html.append("<p class=\"scanned-at\">Scanned at ").append(report.scannedAt()).append("</p></header>");

        html.append("<section class=\"score\"><div class=\"score-value ").append(scoreClass(score.score()))
                .append("\">").append(score.score()).append(" / 100</div>");
        html.append("<div class=\"summary\">");
        appendSummaryBadge(html, "CRITICAL", score.criticals(), "critical");
        appendSummaryBadge(html, "WARNING", score.warnings(), "warning");
        appendSummaryBadge(html, "INFO", score.info(), "info");
        appendSummaryBadge(html, "PASS", score.passed(), "pass");
        html.append("</div></section>");

        Map<String, java.util.List<CheckResult>> byCategory = new LinkedHashMap<>();
        for (CheckResult result : report.results()) {
            byCategory.computeIfAbsent(categoryOf(result.checkId()), k -> new java.util.ArrayList<>()).add(result);
        }

        for (Map.Entry<String, List<CheckResult>> entry : byCategory.entrySet()) {
            html.append("<section class=\"category\"><h2>").append(escape(entry.getKey())).append("</h2><table>");
            html.append("<thead><tr><th>Check</th><th>Severity</th><th>Title</th><th>Description</th>"
                    + "<th>Recommendation</th></tr></thead><tbody>");
            for (CheckResult result : entry.getValue()) {
                html.append("<tr class=\"").append(severityClass(result.severity())).append("\">");
                html.append("<td>").append(escape(result.checkId())).append("</td>");
                html.append("<td><span class=\"badge ").append(severityClass(result.severity())).append("\">")
                        .append(result.severity()).append("</span></td>");
                html.append("<td>").append(escape(result.title())).append("</td>");
                html.append("<td>").append(escape(result.description())).append("</td>");
                html.append("<td>").append(escape(result.recommendation())).append("</td>");
                html.append("</tr>");
            }
            html.append("</tbody></table></section>");
        }

        html.append("</body></html>");
        return html.toString();
    }

    private static void appendSummaryBadge(StringBuilder html, String label, int count, String cssClass) {
        html.append("<span class=\"badge ").append(cssClass).append("\">").append(label).append(' ')
                .append(count).append("</span>");
    }

    private static String categoryOf(String checkId) {
        int dash = checkId.indexOf('-');
        String prefix = dash == -1 ? checkId : checkId.substring(0, dash);
        return CATEGORY_BY_PREFIX.getOrDefault(prefix, prefix);
    }

    private static String severityClass(Severity severity) {
        return severity.name().toLowerCase();
    }

    private static String scoreClass(int score) {
        if (score >= 90) return "pass";
        if (score >= 70) return "warning";
        return "critical";
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private static final String CSS = """
            body { font-family: -apple-system, Segoe UI, Roboto, sans-serif; margin: 0; background: #f6f7f9; color: #1a1a1a; }
            header { background: #1b1f3b; color: white; padding: 24px 32px; }
            header h1 { margin: 0 0 4px; font-size: 24px; }
            header .workspace { margin: 0; opacity: 0.85; font-size: 14px; }
            header .scanned-at { margin: 4px 0 0; opacity: 0.6; font-size: 12px; }
            .score { padding: 24px 32px; }
            .score-value { font-size: 48px; font-weight: 700; margin-bottom: 12px; }
            .score-value.pass { color: #2e7d32; }
            .score-value.warning { color: #b26a00; }
            .score-value.critical { color: #c62828; }
            .badge { display: inline-block; padding: 4px 10px; border-radius: 12px; font-size: 12px; font-weight: 600; margin-right: 8px; }
            .badge.critical { background: #fde0e0; color: #c62828; }
            .badge.warning { background: #fff3cd; color: #b26a00; }
            .badge.info { background: #e0ecff; color: #1a56c4; }
            .badge.pass { background: #e2f4e4; color: #2e7d32; }
            .category { background: white; margin: 16px 32px; padding: 16px 24px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.08); }
            .category h2 { margin-top: 0; font-size: 18px; }
            table { width: 100%; border-collapse: collapse; }
            th, td { text-align: left; padding: 8px 10px; border-bottom: 1px solid #eee; font-size: 13px; vertical-align: top; }
            th { color: #666; font-weight: 600; }
            tr.pass td { color: #999; }
            """;
}
