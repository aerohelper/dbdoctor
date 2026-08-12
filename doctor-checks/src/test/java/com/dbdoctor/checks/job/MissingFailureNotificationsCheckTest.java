/*
 * Copyright 2026 Databricks Doctor contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
