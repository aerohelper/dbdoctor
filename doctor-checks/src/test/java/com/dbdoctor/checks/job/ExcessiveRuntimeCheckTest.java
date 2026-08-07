package com.dbdoctor.checks.job;

import com.dbdoctor.core.model.JobInfo;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExcessiveRuntimeCheckTest {

    private final ExcessiveRuntimeCheck check = new ExcessiveRuntimeCheck(180);

    @Test
    void flagsLongRuntime() {
        JobInfo job = new JobInfo(1L, "slow", null, null, null, null, null, 300L);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(), List.of(job), List.of());

        assertEquals(Severity.WARNING, check.execute(snapshot).severity());
    }

    @Test
    void passesWithinThreshold() {
        JobInfo job = new JobInfo(1L, "quick", null, null, null, null, null, 10L);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(), List.of(job), List.of());

        assertEquals(Severity.PASS, check.execute(snapshot).severity());
    }

    @Test
    void skipsJobWithUnknownDuration() {
        JobInfo job = new JobInfo(1L, "unknown", null, null, null, null, null, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(), List.of(job), List.of());

        assertEquals(Severity.PASS, check.execute(snapshot).severity());
    }
}
