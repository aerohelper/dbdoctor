package com.dbdoctor.core.model;

/**
 * A summary score derived from a set of {@link CheckResult}s. This is dbdoctor's own opinionated
 * score, not an industry-standard metric.
 *
 * @param score     0-100, starting at 100 and deducted per finding (never below 0)
 * @param total     total number of checks that ran
 * @param passed    number of checks with severity PASS
 * @param info      number of checks with severity INFO
 * @param warnings  number of checks with severity WARNING
 * @param criticals number of checks with severity CRITICAL
 */
public record HealthScore(int score, int total, int passed, int info, int warnings, int criticals) {
}
