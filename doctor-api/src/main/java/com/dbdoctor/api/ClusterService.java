package com.dbdoctor.api;

import com.dbdoctor.core.model.ClusterInfo;

import java.util.List;

/** Abstraction over cluster access, so health checks don't depend on the Databricks SDK directly. */
public interface ClusterService {

    /** Returns all clusters visible to the authenticated user in this workspace. */
    List<ClusterInfo> getClusters();
}
