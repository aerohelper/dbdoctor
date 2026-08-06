package com.dbdoctor.checks;

import com.dbdoctor.api.DatabricksApiClient;
import com.dbdoctor.core.model.Finding;

import java.util.List;

/**
 * A single diagnostic check against a Databricks workspace (e.g. a configuration
 * anti-pattern or operational best-practice violation). Implementations should be
 * stateless and safe to run independently of one another.
 */
public interface Check {

    /** Stable identifier used to reference this check in findings and reports. */
    String id();

    /** Short human-readable name of what this check verifies. */
    String name();

    /** Runs the check against the given workspace client, returning zero or more findings. */
    List<Finding> run(DatabricksApiClient client) throws Exception;
}
