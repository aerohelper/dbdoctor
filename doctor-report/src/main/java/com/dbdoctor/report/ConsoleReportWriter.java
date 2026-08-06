package com.dbdoctor.report;

import com.dbdoctor.core.model.Finding;
import com.dbdoctor.core.model.ScanResult;

/** Writes a scan result as human-readable text to stdout. */
public class ConsoleReportWriter implements ReportWriter {

    @Override
    public void write(ScanResult result) {
        System.out.println("dbdoctor scan report — " + result.workspaceUrl());
        System.out.println("Scanned at: " + result.scannedAt());
        System.out.println("Findings: " + result.findings().size());
        for (Finding finding : result.findings()) {
            System.out.printf("  [%s] %s — %s%n", finding.severity(), finding.title(), finding.description());
        }
    }
}
