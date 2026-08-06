package com.dbdoctor.core.model;

import java.time.Instant;
import java.util.List;

/**
 * Aggregate result of running all checks against a Databricks workspace.
 *
 * @param workspaceUrl the workspace that was scanned
 * @param scannedAt    when the scan completed
 * @param findings     all findings produced across every check that ran
 */
public record ScanResult(String workspaceUrl, Instant scannedAt, List<Finding> findings) {
}
