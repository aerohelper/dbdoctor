package com.dbdoctor.checks.cluster;

import com.dbdoctor.core.model.ClusterInfo;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NoInstancePoolCheckTest {

    private final NoInstancePoolCheck check = new NoInstancePoolCheck();

    @Test
    void flagsClusterWithoutPool() {
        ClusterInfo cluster = new ClusterInfo("c1", "unpooled", "RUNNING", "13.3.x", 30, null, null, null, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(cluster), List.of(), List.of());

        assertEquals(Severity.INFO, check.execute(snapshot).severity());
    }

    @Test
    void passesClusterWithPool() {
        ClusterInfo cluster = new ClusterInfo("c1", "pooled", "RUNNING", "13.3.x", 30, null, null, null, "pool-1");
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(cluster), List.of(), List.of());

        assertEquals(Severity.PASS, check.execute(snapshot).severity());
    }
}
