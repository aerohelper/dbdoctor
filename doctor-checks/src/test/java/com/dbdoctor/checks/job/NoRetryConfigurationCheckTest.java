package com.dbdoctor.checks.job;

import com.dbdoctor.core.model.JobInfo;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NoRetryConfigurationCheckTest {

    private final NoRetryConfigurationCheck check = new NoRetryConfigurationCheck();

    @Test
    void flagsJobWithNoRetries() {
        JobInfo job = new JobInfo(1L, "fragile", null, null, null, false, null, null, false);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(), List.of(job), List.of());

        assertEquals(Severity.WARNING, check.execute(snapshot).severity());
    }

    @Test
    void passesJobWithRetriesConfigured() {
        JobInfo job = new JobInfo(1L, "resilient", null, null, null, true, null, null, false);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(), List.of(job), List.of());

        assertEquals(Severity.PASS, check.execute(snapshot).severity());
    }

    @Test
    void skipsJobWithUnknownTaskConfiguration() {
        JobInfo job = new JobInfo(1L, "unknown", null, null, null, null, null, null, false);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(), List.of(job), List.of());

        assertEquals(Severity.PASS, check.execute(snapshot).severity());
    }
}
