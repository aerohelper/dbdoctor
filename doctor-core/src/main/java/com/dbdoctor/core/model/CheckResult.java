package com.dbdoctor.core.model;

/**
 * The outcome of running a single health check.
 *
 * @param checkId        stable identifier of the check, e.g. {@code CLUSTER-001}
 * @param title          short human-readable summary of the finding
 * @param severity       how serious the finding is
 * @param description    detailed explanation of what was found and why it matters
 * @param recommendation actionable guidance on how to resolve the issue
 */
public record CheckResult(
        String checkId,
        String title,
        Severity severity,
        String description,
        String recommendation
) {
}
