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
