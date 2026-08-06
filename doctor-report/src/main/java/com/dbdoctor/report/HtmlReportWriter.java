package com.dbdoctor.report;

import com.dbdoctor.core.model.Finding;
import com.dbdoctor.core.model.ScanResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Writes a scan result as a simple standalone HTML report to the given file path. */
public class HtmlReportWriter implements ReportWriter {

    private final Path outputPath;

    public HtmlReportWriter(Path outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    public void write(ScanResult result) throws IOException {
        StringBuilder html = new StringBuilder();
        html.append("<!doctype html><html><head><meta charset=\"utf-8\"><title>dbdoctor report</title></head><body>");
        html.append("<h1>dbdoctor scan report</h1>");
        html.append("<p>Workspace: ").append(escape(result.workspaceUrl())).append("</p>");
        html.append("<p>Scanned at: ").append(result.scannedAt()).append("</p>");
        html.append("<ul>");
        for (Finding finding : result.findings()) {
            html.append("<li><strong>[").append(finding.severity()).append("] ")
                    .append(escape(finding.title())).append("</strong>: ")
                    .append(escape(finding.description())).append("</li>");
        }
        html.append("</ul></body></html>");

        Files.writeString(outputPath, html.toString(), StandardCharsets.UTF_8);
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
