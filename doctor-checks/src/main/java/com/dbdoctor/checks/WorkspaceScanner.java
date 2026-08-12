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

package com.dbdoctor.checks;

import com.dbdoctor.api.ClusterService;
import com.dbdoctor.api.JobService;
import com.dbdoctor.api.WarehouseService;
import com.dbdoctor.core.model.CheckResult;
import com.dbdoctor.core.model.ScanReport;
import com.dbdoctor.core.model.WorkspaceSnapshot;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Orchestrates a full workspace scan: collect clusters/jobs/warehouses once into a
 * {@link WorkspaceSnapshot}, then run every configured {@link HealthCheck} against it.
 * Collecting up front (rather than each check calling the API independently) avoids
 * redundant API calls.
 */
public class WorkspaceScanner {

    private final String workspaceUrl;
    private final ClusterService clusterService;
    private final JobService jobService;
    private final WarehouseService warehouseService;
    private final List<HealthCheck> checks;

    public WorkspaceScanner(String workspaceUrl, ClusterService clusterService, JobService jobService,
                             WarehouseService warehouseService, List<HealthCheck> checks) {
        this.workspaceUrl = workspaceUrl;
        this.clusterService = clusterService;
        this.jobService = jobService;
        this.warehouseService = warehouseService;
        this.checks = checks;
    }

    /** Collects a fresh snapshot and runs every configured check against it. */
    public ScanReport scan() {
        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(
                clusterService.getClusters(),
                jobService.getJobs(),
                warehouseService.getWarehouses()
        );

        List<CheckResult> results = new ArrayList<>();
        for (HealthCheck check : checks) {
            results.add(check.execute(snapshot));
        }

        return new ScanReport(workspaceUrl, Instant.now(), snapshot, results);
    }
}
