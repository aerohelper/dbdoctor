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

class OversizedWarehouseCheckTest {

    private final OversizedWarehouseCheck check = new OversizedWarehouseCheck();

    @Test
    void flagsLargeWarehouse() {
        WarehouseInfo warehouse = new WarehouseInfo("w1", "big", "RUNNING", "X-Large", 1L, 10L);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(), List.of(), List.of(warehouse));

        assertEquals(Severity.WARNING, check.execute(snapshot).severity());
    }

    @Test
    void passesSmallWarehouse() {
        WarehouseInfo warehouse = new WarehouseInfo("w1", "small", "RUNNING", "2X-Small", 1L, 10L);
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(List.of(), List.of(), List.of(warehouse));

        assertEquals(Severity.PASS, check.execute(snapshot).severity());
    }
}
