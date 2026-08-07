package com.dbdoctor.core.model;

/**
 * dbdoctor's own representation of a Databricks cluster, decoupled from the SDK's model
 * so the rest of the application (and its tests) doesn't depend on Databricks SDK versions.
 *
 * @param id                          cluster ID
 * @param name                        cluster name
 * @param state                       current cluster state, e.g. RUNNING, TERMINATED (as reported by the SDK)
 * @param runtimeVersion              Databricks Runtime version string
 * @param autoTerminationMinutes      configured auto-termination timeout; {@code 0} or {@code null} means disabled
 * @param numWorkers                  configured (fixed) worker count; {@code null} if autoscaling or unavailable
 * @param policyId                    cluster policy ID the cluster is governed by; {@code null} if none
 * @param localDiskEncryptionEnabled  whether local disk encryption is enabled; {@code null} if unknown
 * @param instancePoolId              instance pool ID the cluster draws instances from; {@code null} if none
 */
public record ClusterInfo(
        String id,
        String name,
        String state,
        String runtimeVersion,
        Integer autoTerminationMinutes,
        Long numWorkers,
        String policyId,
        Boolean localDiskEncryptionEnabled,
        String instancePoolId
) {
}
