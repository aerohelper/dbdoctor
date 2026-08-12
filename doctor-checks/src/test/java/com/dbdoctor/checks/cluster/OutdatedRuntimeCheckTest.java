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

package com.dbdoctor.checks.cluster;

import com.dbdoctor.core.model.ClusterInfo;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OutdatedRuntimeCheckTest {

    private final OutdatedRuntimeCheck check = new OutdatedRuntimeCheck(13);

    @Test
    void flagsOldRuntime() {
        ClusterInfo cluster = new ClusterInfo("c1", "old", "RUNNING", "10.4.x-scala2.12", 30, null, null, null, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(cluster), List.of(), List.of());

        assertEquals(Severity.WARNING, check.execute(snapshot).severity());
    }

    @Test
    void passesCurrentRuntime() {
        ClusterInfo cluster = new ClusterInfo("c1", "current", "RUNNING", "13.3.x-scala2.12", 30, null, null, null, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(cluster), List.of(), List.of());

        assertEquals(Severity.PASS, check.execute(snapshot).severity());
    }

    @Test
    void ignoresUnparseableVersionRatherThanFlagging() {
        ClusterInfo cluster = new ClusterInfo("c1", "custom", "RUNNING", "custom-image", 30, null, null, null, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(cluster), List.of(), List.of());

        assertEquals(Severity.PASS, check.execute(snapshot).severity());
    }
}
