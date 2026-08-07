package com.dbdoctor.cli;

import com.databricks.sdk.WorkspaceClient;
import com.dbdoctor.api.DatabricksClusterService;
import com.dbdoctor.api.DatabricksJobService;
import com.dbdoctor.api.DatabricksWarehouseService;
import com.dbdoctor.checks.HealthCheck;
import com.dbdoctor.checks.cluster.AutoTerminationCheck;
import com.dbdoctor.checks.cluster.ExcessiveAutoTerminationCheck;
import com.dbdoctor.checks.cluster.MissingClusterPolicyCheck;
import com.dbdoctor.checks.cluster.OutdatedRuntimeCheck;
import com.dbdoctor.checks.cluster.OversizedClusterCheck;
import com.dbdoctor.checks.job.ExcessiveRuntimeCheck;
import com.dbdoctor.checks.job.NoRetryConfigurationCheck;
import com.dbdoctor.checks.job.RepeatedFailuresCheck;
import com.dbdoctor.checks.warehouse.AutoStopDisabledCheck;
import com.dbdoctor.checks.warehouse.OversizedWarehouseCheck;
import com.dbdoctor.core.model.CheckResult;
import com.dbdoctor.core.model.WorkspaceSnapshot;
import picocli.CommandLine.Command;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Scans a Databricks workspace and runs all available health checks against it.
 * Authentication is resolved the same way as {@code dbdoctor auth test} — via the
 * Databricks SDK's unified authentication (env vars, {@code ~/.databrickscfg}, etc.).
 *
 * <p>This is a minimal runner: it prints each check's raw result. The scoring engine
 * and console/JSON/HTML report formats are later MVP phases.
 */
@Command(name = "scan", description = "Scan a Databricks workspace for configuration issues and anti-patterns.")
public class ScanCommand implements Callable<Integer> {

    @Override
    public Integer call() {
        WorkspaceClient sdk = new WorkspaceClient();

        WorkspaceSnapshot snapshot = new WorkspaceSnapshot(
                new DatabricksClusterService(sdk).getClusters(),
                new DatabricksJobService(sdk).getJobs(),
                new DatabricksWarehouseService(sdk).getWarehouses()
        );

        List<HealthCheck> checks = List.of(
                new AutoTerminationCheck(),
                new ExcessiveAutoTerminationCheck(),
                new OutdatedRuntimeCheck(),
                new MissingClusterPolicyCheck(),
                new OversizedClusterCheck(),
                new NoRetryConfigurationCheck(),
                new RepeatedFailuresCheck(),
                new ExcessiveRuntimeCheck(),
                new AutoStopDisabledCheck(),
                new OversizedWarehouseCheck()
        );

        System.out.println("dbdoctor scan — " + sdk.config().getHost());
        System.out.printf("Clusters: %d, Jobs: %d, Warehouses: %d%n%n",
                snapshot.clusters().size(), snapshot.jobs().size(), snapshot.warehouses().size());

        for (HealthCheck check : checks) {
            CheckResult result = check.execute(snapshot);
            System.out.printf("[%s] %-8s %s — %s%n",
                    result.checkId(), result.severity(), result.title(), result.description());
        }

        return 0;
    }
}
