package com.dbdoctor.api;

import com.databricks.sdk.WorkspaceClient;
import com.databricks.sdk.service.sql.EndpointInfo;
import com.databricks.sdk.service.sql.ListWarehousesRequest;
import com.dbdoctor.core.model.WarehouseInfo;

import java.util.ArrayList;
import java.util.List;

/** {@link WarehouseService} backed by the Databricks SDK. */
public class DatabricksWarehouseService implements WarehouseService {

    private final WorkspaceClient workspaceClient;

    public DatabricksWarehouseService(WorkspaceClient workspaceClient) {
        this.workspaceClient = workspaceClient;
    }

    @Override
    public List<WarehouseInfo> getWarehouses() {
        List<WarehouseInfo> warehouses = new ArrayList<>();
        workspaceClient.warehouses().list(new ListWarehousesRequest())
                .forEach(endpoint -> warehouses.add(toWarehouseInfo(endpoint)));
        return warehouses;
    }

    private static WarehouseInfo toWarehouseInfo(EndpointInfo endpoint) {
        return new WarehouseInfo(
                endpoint.getId(),
                endpoint.getName(),
                endpoint.getState() == null ? null : endpoint.getState().toString(),
                endpoint.getClusterSize(),
                endpoint.getNumClusters(),
                endpoint.getAutoStopMins()
        );
    }
}
