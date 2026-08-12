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
