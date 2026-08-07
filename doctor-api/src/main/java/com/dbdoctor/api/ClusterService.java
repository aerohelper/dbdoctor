package com.dbdoctor.api;

import com.databricks.sdk.service.compute.ClusterDetails;

import java.util.List;

/** Abstraction over cluster access, so health checks don't depend on the Databricks SDK directly. */
public interface ClusterService {

    /** Returns all clusters visible to the authenticated user in this workspace. */
    List<ClusterDetails> getClusters();
}
