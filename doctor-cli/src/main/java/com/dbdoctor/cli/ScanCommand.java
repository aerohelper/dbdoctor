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

package com.dbdoctor.cli;

import com.databricks.sdk.WorkspaceClient;
import com.dbdoctor.api.DatabricksClusterService;
import com.dbdoctor.api.DatabricksJobService;
import com.dbdoctor.api.DatabricksWarehouseService;
import com.dbdoctor.checks.HealthCheckRegistry;
import com.dbdoctor.checks.WorkspaceScanner;
import com.dbdoctor.core.config.DoctorConfig;
import com.dbdoctor.core.model.HealthScore;
import com.dbdoctor.core.model.ScanReport;
import com.dbdoctor.core.score.ScoreCalculator;
import com.dbdoctor.report.ConsoleReportRenderer;
import com.dbdoctor.report.HtmlReportRenderer;
import com.dbdoctor.report.JsonReportRenderer;
import com.dbdoctor.report.ReportRenderer;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

/**
 * Scans a Databricks workspace and runs every registered health check against it.
 * Authentication is resolved the same way as {@code dbdoctor auth test} — via the
 * Databricks SDK's unified authentication (env vars, {@code ~/.databrickscfg}, etc.).
 */
@Command(name = "scan", description = "Scan a Databricks workspace for configuration issues and anti-patterns.")
public class ScanCommand implements Callable<Integer> {

    @Option(names = {"-f", "--format"}, description = "Report format: console, json, html (default: console)")
    private String format = "console";

    @Option(names = {"-o", "--output"}, description = "Write the report to this file instead of stdout")
    private Path outputPath;

    @Option(names = {"-c", "--config"}, description = "Path to a dbdoctor.yml config file overriding check thresholds")
    private Path configPath;

    @Override
    public Integer call() throws IOException {
        WorkspaceClient sdk = new WorkspaceClient();
        DoctorConfig config = ConfigLoader.load(configPath);

        WorkspaceScanner scanner = new WorkspaceScanner(
                sdk.config().getHost(),
                new DatabricksClusterService(sdk),
                new DatabricksJobService(sdk),
                new DatabricksWarehouseService(sdk),
                HealthCheckRegistry.from(config)
        );

        ScanReport report = scanner.scan();
        HealthScore score = new ScoreCalculator().calculate(report.results());

        ReportRenderer renderer = switch (format.toLowerCase()) {
            case "console" -> new ConsoleReportRenderer();
            case "json" -> new JsonReportRenderer();
            case "html" -> new HtmlReportRenderer();
            default -> throw new IllegalArgumentException("Unknown report format: " + format);
        };

        String rendered = renderer.render(report, score);

        if (outputPath != null) {
            Files.writeString(outputPath, rendered, StandardCharsets.UTF_8);
            System.out.println("Report written to " + outputPath);
        } else {
            System.out.println(rendered);
        }

        return score.criticals() > 0 ? 1 : 0;
    }
}
