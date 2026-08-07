package com.dbdoctor.checks.cluster;

import com.dbdoctor.core.model.ClusterInfo;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalDiskEncryptionDisabledCheckTest {

    private final LocalDiskEncryptionDisabledCheck check = new LocalDiskEncryptionDisabledCheck();

    @Test
    void flagsDisabledEncryption() {
        ClusterInfo cluster = new ClusterInfo("c1", "unencrypted", "RUNNING", "13.3.x", 30, null, null, false, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(cluster), List.of(), List.of());

        assertEquals(Severity.WARNING, check.execute(snapshot).severity());
    }

    @Test
    void passesEnabledEncryption() {
        ClusterInfo cluster = new ClusterInfo("c1", "encrypted", "RUNNING", "13.3.x", 30, null, null, true, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(cluster), List.of(), List.of());

        assertEquals(Severity.PASS, check.execute(snapshot).severity());
    }

    @Test
    void treatsUnknownAsDisabled() {
        ClusterInfo cluster = new ClusterInfo("c1", "unknown", "RUNNING", "13.3.x", 30, null, null, null, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(cluster), List.of(), List.of());

        assertEquals(Severity.WARNING, check.execute(snapshot).severity());
    }
}
