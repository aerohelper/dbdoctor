package com.dbdoctor.checks;

import com.dbdoctor.checks.cluster.AutoTerminationCheck;
import com.dbdoctor.checks.cluster.ExcessiveAutoTerminationCheck;
import com.dbdoctor.checks.cluster.LocalDiskEncryptionDisabledCheck;
import com.dbdoctor.checks.cluster.MissingClusterPolicyCheck;
import com.dbdoctor.checks.cluster.NoInstancePoolCheck;
import com.dbdoctor.checks.cluster.OutdatedRuntimeCheck;
import com.dbdoctor.checks.cluster.OversizedClusterCheck;
import com.dbdoctor.checks.job.ExcessiveRuntimeCheck;
import com.dbdoctor.checks.job.MissingFailureNotificationsCheck;
import com.dbdoctor.checks.job.NoRetryConfigurationCheck;
import com.dbdoctor.checks.job.NoTimeoutConfiguredCheck;
import com.dbdoctor.checks.job.RepeatedFailuresCheck;
import com.dbdoctor.checks.warehouse.AutoStopDisabledCheck;
import com.dbdoctor.checks.warehouse.OversizedWarehouseCheck;
import com.dbdoctor.core.config.DoctorConfig;

import java.util.List;

/** The single source of truth for "every check dbdoctor knows how to run". */
public final class HealthCheckRegistry {

    private HealthCheckRegistry() {
    }

    /** Every built-in check, with its documented default threshold. */
    public static List<HealthCheck> defaults() {
        return from(new DoctorConfig());
    }

    /** Every built-in check, with thresholds taken from the given configuration. */
    public static List<HealthCheck> from(DoctorConfig config) {
        DoctorConfig.ClusterConfig cluster = config.checks.cluster;
        DoctorConfig.JobConfig job = config.checks.job;
        DoctorConfig.WarehouseConfig warehouse = config.checks.warehouse;

        return List.of(
                new AutoTerminationCheck(),
                new ExcessiveAutoTerminationCheck(cluster.maxAutoTerminationMinutes),
                new OutdatedRuntimeCheck(cluster.minSupportedRuntimeMajorVersion),
                new MissingClusterPolicyCheck(),
                new OversizedClusterCheck(cluster.maxWorkers),
                new LocalDiskEncryptionDisabledCheck(),
                new NoInstancePoolCheck(),
                new NoRetryConfigurationCheck(),
                new RepeatedFailuresCheck(job.failureThreshold),
                new ExcessiveRuntimeCheck(job.maxRuntimeMinutes),
                new NoTimeoutConfiguredCheck(),
                new MissingFailureNotificationsCheck(),
                new AutoStopDisabledCheck(),
                new OversizedWarehouseCheck(warehouse.largeSizes)
        );
    }
}
