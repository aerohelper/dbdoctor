package com.dbdoctor.api;

import com.dbdoctor.core.model.WarehouseInfo;

import java.util.List;

/** Abstraction over SQL warehouse access, so health checks don't depend on the Databricks SDK directly. */
public interface WarehouseService {

    /** Returns all SQL warehouses visible to the authenticated user in this workspace. */
    List<WarehouseInfo> getWarehouses();
}
