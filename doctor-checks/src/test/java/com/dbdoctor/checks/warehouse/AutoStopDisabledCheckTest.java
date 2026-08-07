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
