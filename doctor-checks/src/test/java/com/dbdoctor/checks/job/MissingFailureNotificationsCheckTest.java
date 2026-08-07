package com.dbdoctor.checks.job;

import com.dbdoctor.core.model.JobInfo;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MissingFailureNotificationsCheckTest {

    private final MissingFailureNotificationsCheck check = new MissingFailureNotificationsCheck();

    @Test
    void flagsMissingNotifications() {
        JobInfo job = new JobInfo(1L, "silent", null, null, null, null, null, null, false);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(), List.of(job), List.of());

        assertEquals(Severity.WARNING, check.execute(snapshot).severity());
    }

    @Test
    void passesConfiguredNotifications() {
        JobInfo job = new JobInfo(1L, "monitored", null, null, null, null, null, null, true);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(), List.of(job), List.of());

        assertEquals(Severity.PASS, check.execute(snapshot).severity());
    }
}
