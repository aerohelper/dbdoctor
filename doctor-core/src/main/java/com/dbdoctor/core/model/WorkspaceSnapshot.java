package com.dbdoctor.core.model;

import java.util.List;

/**
 * A point-in-time snapshot of the workspace resources that health checks run against.
 * Collecting everything up front (rather than each check calling the API independently)
 * avoids redundant API calls and lets checks run purely against in-memory data.
 *
 * @param clusters   all clusters in the workspace at snapshot time
 * @param jobs       all jobs in the workspace at snapshot time
 * @param warehouses all SQL warehouses in the workspace at snapshot time
 */
public record WorkspaceSnapshot(
        List<ClusterInfo> clusters,
        List<JobInfo> jobs,
        List<WarehouseInfo> warehouses
) {
}
