package com.dbdoctor.cli;

import com.dbdoctor.core.config.DoctorConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigLoaderTest {

    @Test
    void returnsAllDefaultsWhenNoPathGiven() throws IOException {
        DoctorConfig config = ConfigLoader.load(null);

        assertEquals(120, config.checks.cluster.maxAutoTerminationMinutes);
        assertEquals(180, config.checks.job.maxRuntimeMinutes);
    }

    @Test
    void loadsPartialYamlOverridesAndKeepsRestAtDefault(@TempDir Path tempDir) throws IOException {
        Path yaml = tempDir.resolve("dbdoctor.yml");
        Files.writeString(yaml, """
                checks:
                  cluster:
                    maxAutoTerminationMinutes: 60
                  job:
                    maxRuntimeMinutes: 90
                """);

        DoctorConfig config = ConfigLoader.load(yaml);

        assertEquals(60, config.checks.cluster.maxAutoTerminationMinutes);
        assertEquals(90, config.checks.job.maxRuntimeMinutes);
        // Not present in the YAML — should keep its built-in default.
        assertEquals(13, config.checks.cluster.minSupportedRuntimeMajorVersion);
        assertEquals(10, config.checks.cluster.maxWorkers);
    }
}
