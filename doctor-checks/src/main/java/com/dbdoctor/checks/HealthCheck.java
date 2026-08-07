package com.dbdoctor.checks;

import com.dbdoctor.core.model.CheckResult;
import com.dbdoctor.core.model.WorkspaceSnapshot;

/**
 * A single health check run against a {@link WorkspaceSnapshot}. Implementations should be
 * stateless, side-effect free, and read-only — dbdoctor never modifies the workspace it scans.
 */
public interface HealthCheck {

    /** Runs this check against the given workspace snapshot and returns its result. */
    CheckResult execute(WorkspaceSnapshot workspace);
}
