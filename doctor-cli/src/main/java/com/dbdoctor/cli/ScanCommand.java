package com.dbdoctor.cli;

import com.dbdoctor.checks.Check;
import com.dbdoctor.checks.PlaceholderCheck;
import com.dbdoctor.api.DatabricksApiClient;
import com.dbdoctor.core.model.Finding;
import com.dbdoctor.core.model.ScanResult;
import com.dbdoctor.report.ConsoleReportWriter;
import com.dbdoctor.report.HtmlReportWriter;
import com.dbdoctor.report.JsonReportWriter;
import com.dbdoctor.report.ReportWriter;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/** Runs all diagnostic checks against a Databricks workspace and emits a report. */
@Command(name = "scan", description = "Scan a Databricks workspace for configuration issues and anti-patterns.")
public class ScanCommand implements Callable<Integer> {

    @Option(names = {"-w", "--workspace-url"}, required = true, description = "Databricks workspace URL, e.g. https://my-workspace.cloud.databricks.com")
    private String workspaceUrl;

    @Option(names = {"-t", "--token"}, required = true, description = "Databricks personal access token", arity = "1")
    private String token;

    @Option(names = {"-f", "--format"}, description = "Report format: console, json, html (default: console)")
    private String format = "console";

    @Option(names = {"-o", "--output"}, description = "Output file path (required for json/html formats)")
    private Path outputPath;

    @Override
    public Integer call() throws Exception {
        DatabricksApiClient client = new DatabricksApiClient(workspaceUrl, token);

        List<Check> checks = List.of(new PlaceholderCheck());
        List<Finding> findings = new ArrayList<>();
        for (Check check : checks) {
            findings.addAll(check.run(client));
        }

        ScanResult result = new ScanResult(workspaceUrl, Instant.now(), findings);

        ReportWriter writer = switch (format.toLowerCase()) {
            case "json" -> new JsonReportWriter(requireOutputPath());
            case "html" -> new HtmlReportWriter(requireOutputPath());
            case "console" -> new ConsoleReportWriter();
            default -> throw new IllegalArgumentException("Unknown report format: " + format);
        };
        writer.write(result);

        return 0;
    }

    private Path requireOutputPath() {
        if (outputPath == null) {
            throw new IllegalArgumentException("--output is required for format '" + format + "'");
        }
        return outputPath;
    }
}
