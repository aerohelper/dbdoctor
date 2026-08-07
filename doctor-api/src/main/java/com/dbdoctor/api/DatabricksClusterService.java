package com.dbdoctor.api;

import com.databricks.sdk.WorkspaceClient;
import com.databricks.sdk.service.compute.ClusterDetails;
import com.databricks.sdk.service.compute.ListClustersRequest;
import com.dbdoctor.core.model.ClusterInfo;

import java.util.ArrayList;
import java.util.List;

/** {@link ClusterService} backed by the Databricks SDK. */
public class DatabricksClusterService implements ClusterService {

    private final WorkspaceClient workspaceClient;

    public DatabricksClusterService(WorkspaceClient workspaceClient) {
        this.workspaceClient = workspaceClient;
    }

    @Override
    public List<ClusterInfo> getClusters() {
        List<ClusterInfo> clusters = new ArrayList<>();
        workspaceClient.clusters().list(new ListClustersRequest())
                .forEach(details -> clusters.add(toClusterInfo(details)));
        return clusters;
    }

    private static ClusterInfo toClusterInfo(ClusterDetails details) {
        Long autoTermination = details.getAutoterminationMinutes();
        return new ClusterInfo(
                details.getClusterId(),
                details.getClusterName(),
                details.getState() == null ? null : details.getState().toString(),
                details.getSparkVersion(),
                autoTermination == null ? null : autoTermination.intValue(),
                details.getNumWorkers(),
                details.getPolicyId()
        );
    }
}
