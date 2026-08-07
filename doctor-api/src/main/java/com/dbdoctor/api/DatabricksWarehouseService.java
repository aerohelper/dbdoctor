package com.dbdoctor.api;

import com.databricks.sdk.WorkspaceClient;
import com.databricks.sdk.service.sql.EndpointInfo;
import com.databricks.sdk.service.sql.ListWarehousesRequest;

import java.util.ArrayList;
import java.util.List;

/** {@link WarehouseService} backed by the Databricks SDK. */
public class DatabricksWarehouseService implements WarehouseService {

    private final WorkspaceClient workspaceClient;

    public DatabricksWarehouseService(WorkspaceClient workspaceClient) {
        this.workspaceClient = workspaceClient;
    }

    @Override
    public List<EndpointInfo> getWarehouses() {
        List<EndpointInfo> warehouses = new ArrayList<>();
        workspaceClient.warehouses().list(new ListWarehousesRequest()).forEach(warehouses::add);
        return warehouses;
    }
}
