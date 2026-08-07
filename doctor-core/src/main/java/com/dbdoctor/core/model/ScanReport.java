package com.dbdoctor.core.model;

import java.time.Instant;
import java.util.List;

/**
 * The full result of a workspace scan: the snapshot that was collected and every
 * check result produced against it.
 *
 * @param workspaceUrl the workspace that was scanned
 * @param scannedAt     when the scan completed
 * @param snapshot      the workspace data the checks ran against
 * @param results       one result per check that ran
 */
public record ScanReport(
        String workspaceUrl,
        Instant scannedAt,
        WorkspaceSnapshot snapshot,
        List<CheckResult> results
) {
}
