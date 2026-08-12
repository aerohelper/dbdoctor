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

package com.dbdoctor.checks;

import com.dbdoctor.core.config.DoctorConfig;
import com.dbdoctor.core.model.ClusterInfo;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HealthCheckRegistryTest {

    @Test
    void defaultsReturnsAllFourteenChecks() {
        assertEquals(14, HealthCheckRegistry.defaults().size());
    }

    @Test
    void configuredThresholdIsHonoredByRegisteredCheck() {
        DoctorConfig config = new DoctorConfig();
        config.checks.cluster.maxAutoTerminationMinutes = 30;

        List<HealthCheck> checks = HealthCheckRegistry.from(config);

        // A cluster with 45-minute auto termination should trip CLUSTER-002 at a 30-minute threshold,
        // even though it's well within the built-in 120-minute default.
        ClusterInfo cluster = new ClusterInfo("c1", "borderline", "RUNNING", "13.3.x", 45, null, null, null, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(cluster), List.of(), List.of());

        boolean tripped = checks.stream()
                .filter(c -> c.execute(snapshot).checkId().equals("CLUSTER-002"))
                .anyMatch(c -> c.execute(snapshot).severity() == Severity.WARNING);

        assertTrue(tripped);
    }
}
