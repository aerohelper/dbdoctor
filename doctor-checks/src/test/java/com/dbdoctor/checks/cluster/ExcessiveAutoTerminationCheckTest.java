package com.dbdoctor.checks.cluster;

import com.dbdoctor.core.model.CheckResult;
import com.dbdoctor.core.model.ClusterInfo;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExcessiveAutoTerminationCheckTest {

    @Test
    void flagsAboveThreshold() {
        ExcessiveAutoTerminationCheck check = new ExcessiveAutoTerminationCheck(60);
        ClusterInfo cluster = new ClusterInfo("c1", "long-lived", "RUNNING", "13.3.x", 200, null, null, null, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(cluster), List.of(), List.of());

        assertEquals(Severity.WARNING, check.execute(snapshot).severity());
    }

    @Test
    void passesAtOrBelowThreshold() {
        ExcessiveAutoTerminationCheck check = new ExcessiveAutoTerminationCheck(60);
        ClusterInfo cluster = new ClusterInfo("c1", "fine", "RUNNING", "13.3.x", 60, null, null, null, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(cluster), List.of(), List.of());

        assertEquals(Severity.PASS, check.execute(snapshot).severity());
    }

    @Test
    void ignoresDisabledAutoTermination() {
        // CLUSTER-001's job, not this check's — a null/0 value here should not also trip CLUSTER-002.
        ExcessiveAutoTerminationCheck check = new ExcessiveAutoTerminationCheck(60);
        ClusterInfo cluster = new ClusterInfo("c1", "disabled", "RUNNING", "13.3.x", null, null, null, null, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(cluster), List.of(), List.of());

        assertEquals(Severity.PASS, check.execute(snapshot).severity());
    }

    @Test
    void defaultThresholdIs120Minutes() {
        CheckResult result = new ExcessiveAutoTerminationCheck().execute(
                new WorkspaceSnapshot(List.of(), List.of(), List.of()));

        assertEquals("CLUSTER-002", result.checkId());
    }
}
