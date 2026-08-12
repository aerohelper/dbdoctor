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

package com.dbdoctor.checks.warehouse;

import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WarehouseInfo;
import com.dbdoctor.core.model.WorkspaceSnapshot;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AutoStopDisabledCheckTest {

    private final AutoStopDisabledCheck check = new AutoStopDisabledCheck();

    @Test
    void flagsDisabledAutoStop() {
        WarehouseInfo warehouse = new WarehouseInfo("w1", "always-on", "RUNNING", "Small", 1L, 0L);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(), List.of(), List.of(warehouse));

        assertEquals(Severity.CRITICAL, check.execute(snapshot).severity());
    }

    @Test
    void passesConfiguredAutoStop() {
        WarehouseInfo warehouse = new WarehouseInfo("w1", "fine", "RUNNING", "Small", 1L, 10L);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(), List.of(), List.of(warehouse));

        assertEquals(Severity.PASS, check.execute(snapshot).severity());
    }

    @Test
    void handlesNullAutoStop() {
        WarehouseInfo warehouse = new WarehouseInfo("w1", "unknown", "RUNNING", "Small", 1L, null);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(), List.of(), List.of(warehouse));

        assertEquals(Severity.CRITICAL, check.execute(snapshot).severity());
    }
}
