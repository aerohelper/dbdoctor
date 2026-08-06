package com.dbdoctor.core.model;

/**
 * A single diagnostic finding produced by a check.
 *
 * @param checkId     stable identifier of the check that produced this finding
 * @param severity    how serious the finding is
 * @param title       short human-readable summary
 * @param description detailed explanation and recommendation
 */
public record Finding(String checkId, Severity severity, String title, String description) {
}
