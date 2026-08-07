package com.dbdoctor.checks.job;

import com.dbdoctor.core.model.JobInfo;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RepeatedFailuresCheckTest {

    private final RepeatedFailuresCheck check = new RepeatedFailuresCheck(3);

    @Test
    void flagsRepeatedFailures() {
        JobInfo job = new JobInfo(1L, "broken", null, null, null, null, 5, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(), List.of(job), List.of());

        assertEquals(Severity.CRITICAL, check.execute(snapshot).severity());
    }

    @Test
    void passesBelowThreshold() {
        JobInfo job = new JobInfo(1L, "mostly-fine", null, null, null, null, 1, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(), List.of(job), List.of());

        assertEquals(Severity.PASS, check.execute(snapshot).severity());
    }

    @Test
    void skipsJobWithUnknownRunHistory() {
        JobInfo job = new JobInfo(1L, "unknown", null, null, null, null, null, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(), List.of(job), List.of());

        assertEquals(Severity.PASS, check.execute(snapshot).severity());
    }
}
