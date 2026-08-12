/*
 * Copyright 2026 Databricks Doctor contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
