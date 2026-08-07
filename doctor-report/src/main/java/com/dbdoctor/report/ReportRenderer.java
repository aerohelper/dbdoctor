package com.dbdoctor.report;

import com.dbdoctor.core.model.HealthScore;
import com.dbdoctor.core.model.ScanReport;

/** Renders a {@link ScanReport} and its {@link HealthScore} into a specific output format. */
public interface ReportRenderer {

    /** Renders the report as a string. Callers decide whether to print it or write it to a file. */
    String render(ScanReport report, HealthScore score);
}
