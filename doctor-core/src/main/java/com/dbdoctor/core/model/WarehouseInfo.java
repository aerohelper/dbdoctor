package com.dbdoctor.core.model;

/**
 * dbdoctor's own representation of a Databricks SQL warehouse, decoupled from the SDK's model.
 *
 * @param id               warehouse ID
 * @param name             warehouse name
 * @param state             current warehouse state, e.g. RUNNING, STOPPED (as reported by the SDK)
 * @param clusterSize      warehouse cluster size, e.g. "2X-Small"
 * @param numClusters      number of clusters (for multi-cluster load balancing)
 * @param autoStopMinutes  configured auto-stop timeout; {@code 0} or {@code null} means disabled
 */
public record WarehouseInfo(
        String id,
        String name,
        String state,
        String clusterSize,
        Long numClusters,
        Long autoStopMinutes
) {
}
