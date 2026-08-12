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

class RepeatedFailuresCheckTest {

    private final RepeatedFailuresCheck check = new RepeatedFailuresCheck(3);

    @Test
    void flagsRepeatedFailures() {
        JobInfo job = new JobInfo(1L, "broken", null, null, null, null, 5, null, false);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(), List.of(job), List.of());

        assertEquals(Severity.CRITICAL, check.execute(snapshot).severity());
    }

    @Test
    void passesBelowThreshold() {
        JobInfo job = new JobInfo(1L, "mostly-fine", null, null, null, null, 1, null, false);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(), List.of(job), List.of());

        assertEquals(Severity.PASS, check.execute(snapshot).severity());
    }

    @Test
    void skipsJobWithUnknownRunHistory() {
        JobInfo job = new JobInfo(1L, "unknown", null, null, null, null, null, null, false);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(), List.of(job), List.of());

        assertEquals(Severity.PASS, check.execute(snapshot).severity());
    }
}
