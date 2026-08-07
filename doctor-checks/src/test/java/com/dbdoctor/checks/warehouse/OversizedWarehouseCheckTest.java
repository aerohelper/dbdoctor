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
