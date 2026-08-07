package com.dbdoctor.api;

import com.databricks.sdk.WorkspaceClient;
import com.databricks.sdk.service.compute.ClusterDetails;
import com.databricks.sdk.service.compute.ListClustersRequest;

import java.util.ArrayList;
import java.util.List;

/** {@link ClusterService} backed by the Databricks SDK. */
public class DatabricksClusterService implements ClusterService {

    private final WorkspaceClient workspaceClient;

    public DatabricksClusterService(WorkspaceClient workspaceClient) {
        this.workspaceClient = workspaceClient;
    }

    @Override
    public List<ClusterDetails> getClusters() {
        List<ClusterDetails> clusters = new ArrayList<>();
        workspaceClient.clusters().list(new ListClustersRequest()).forEach(clusters::add);
        return clusters;
    }
}
