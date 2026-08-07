package com.dbdoctor.checks.cluster;

import com.dbdoctor.core.model.ClusterInfo;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OversizedClusterCheckTest {

    private final OversizedClusterCheck check = new OversizedClusterCheck(10);

    @Test
    void flagsTooManyWorkers() {
        ClusterInfo cluster = new ClusterInfo("c1", "huge", "RUNNING", "13.3.x", 30, 50L, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(cluster), List.of(), List.of());

        assertEquals(Severity.WARNING, check.execute(snapshot).severity());
    }

    @Test
    void passesWithinLimit() {
        ClusterInfo cluster = new ClusterInfo("c1", "normal", "RUNNING", "13.3.x", 30, 4L, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(cluster), List.of(), List.of());

        assertEquals(Severity.PASS, check.execute(snapshot).severity());
    }

    @Test
    void ignoresAutoscalingClustersWithUnknownWorkerCount() {
        ClusterInfo cluster = new ClusterInfo("c1", "autoscale", "RUNNING", "13.3.x", 30, null, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(cluster), List.of(), List.of());

        assertEquals(Severity.PASS, check.execute(snapshot).severity());
    }
}
