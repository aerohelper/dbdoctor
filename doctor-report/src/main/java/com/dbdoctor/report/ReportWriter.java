package com.dbdoctor.report;

import com.dbdoctor.core.model.ScanResult;

import java.io.IOException;

/** Renders a {@link ScanResult} into a specific output format (console, JSON, HTML, ...). */
public interface ReportWriter {

    /** Renders the scan result and writes it to the given target (path or stream, TBD by implementation). */
    void write(ScanResult result) throws IOException;
}
