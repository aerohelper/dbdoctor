package com.dbdoctor.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.dbdoctor.core.model.ScanResult;

import java.io.IOException;
import java.nio.file.Path;

/** Writes a scan result as JSON to the given file path. */
public class JsonReportWriter implements ReportWriter {

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .enable(SerializationFeature.INDENT_OUTPUT);

    private final Path outputPath;

    public JsonReportWriter(Path outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    public void write(ScanResult result) throws IOException {
        mapper.writeValue(outputPath.toFile(), result);
    }
}
