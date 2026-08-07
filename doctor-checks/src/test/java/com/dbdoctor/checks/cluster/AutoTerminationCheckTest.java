package com.dbdoctor.checks.cluster;

import com.dbdoctor.core.model.CheckResult;
import com.dbdoctor.core.model.ClusterInfo;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AutoTerminationCheckTest {

    private final AutoTerminationCheck check = new AutoTerminationCheck();

    @Test
    void detectsDisabledAutoTermination() {
        ClusterInfo cluster = new ClusterInfo("c1", "risky", "RUNNING", "13.3.x", 0, null, null, null, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(cluster), List.of(), List.of());

        CheckResult result = check.execute(snapshot);

        assertEquals(Severity.CRITICAL, result.severity());
        assertTrue(result.description().contains("risky"));
    }

    @Test
    void acceptsConfiguredAutoTermination() {
        ClusterInfo cluster = new ClusterInfo("c1", "safe", "RUNNING", "13.3.x", 30, null, null, null, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(cluster), List.of(), List.of());

        CheckResult result = check.execute(snapshot);

        assertEquals(Severity.PASS, result.severity());
    }

    @Test
    void handlesNullConfiguration() {
        ClusterInfo cluster = new ClusterInfo("c1", "unknown", "RUNNING", "13.3.x", null, null, null, null, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(cluster), List.of(), List.of());

        CheckResult result = check.execute(snapshot);

        assertEquals(Severity.CRITICAL, result.severity());
    }

    @Test
    void returnsCorrectCheckId() {
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(), List.of(), List.of());

        CheckResult result = check.execute(snapshot);

        assertEquals("CLUSTER-001", result.checkId());
    }
}
