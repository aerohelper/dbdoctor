package com.dbdoctor.checks.cluster;

import com.dbdoctor.core.model.ClusterInfo;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MissingClusterPolicyCheckTest {

    private final MissingClusterPolicyCheck check = new MissingClusterPolicyCheck();

    @Test
    void flagsMissingPolicy() {
        ClusterInfo cluster = new ClusterInfo("c1", "unmanaged", "RUNNING", "13.3.x", 30, null, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(cluster), List.of(), List.of());

        assertEquals(Severity.WARNING, check.execute(snapshot).severity());
    }

    @Test
    void passesWhenPolicyPresent() {
        ClusterInfo cluster = new ClusterInfo("c1", "managed", "RUNNING", "13.3.x", 30, null, "policy-123");
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(cluster), List.of(), List.of());

        assertEquals(Severity.PASS, check.execute(snapshot).severity());
    }
}
