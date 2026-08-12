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
