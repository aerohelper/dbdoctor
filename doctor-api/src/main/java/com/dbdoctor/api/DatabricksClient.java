package com.dbdoctor.api;

import com.databricks.sdk.WorkspaceClient;
import com.databricks.sdk.service.compute.ClusterDetails;
import com.databricks.sdk.service.compute.ListClustersRequest;
import com.databricks.sdk.service.iam.User;

/**
 * Wraps the Databricks SDK's {@link WorkspaceClient}, providing the operations
 * dbdoctor's checks and CLI commands need. Authentication is resolved by the SDK
 * itself from the environment (env vars, {@code ~/.databrickscfg} profile, etc.) —
 * see the Databricks CLI's {@code auth login} command for setting that up.
 */
public class DatabricksClient {

    private final WorkspaceClient workspaceClient;

    /** Creates a client using the SDK's default authentication resolution. */
    public DatabricksClient() {
        this(new WorkspaceClient());
    }

    public DatabricksClient(WorkspaceClient workspaceClient) {
        this.workspaceClient = workspaceClient;
    }

    /** The workspace URL this client is connected to. */
    public String workspaceUrl() {
        return workspaceClient.config().getHost();
    }

    /** The currently authenticated user's username (typically an email address). */
    public String currentUserName() {
        User user = workspaceClient.currentUser().me();
        return user.getUserName();
    }

    /** Lists all clusters visible to the authenticated user in this workspace. */
    public Iterable<ClusterDetails> listClusters() {
        return workspaceClient.clusters().list(new ListClustersRequest());
    }

    /** Access to the full underlying SDK client for capabilities not yet wrapped here. */
    public WorkspaceClient sdk() {
        return workspaceClient;
    }
}
