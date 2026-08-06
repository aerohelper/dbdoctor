package com.dbdoctor.checks;

import com.dbdoctor.api.DatabricksApiClient;
import com.dbdoctor.core.model.Finding;
import com.dbdoctor.core.model.Severity;

import java.util.List;

/**
 * Placeholder check used to validate the checks pipeline end-to-end.
 * Real checks (cluster config, Delta table health, Spark SQL anti-patterns, etc.)
 * will replace/join this once the diagnostic logic is built out.
 */
public class PlaceholderCheck implements Check {

    @Override
    public String id() {
        return "placeholder-check";
    }

    @Override
    public String name() {
        return "Placeholder Check";
    }

    @Override
    public List<Finding> run(DatabricksApiClient client) {
        return List.of(new Finding(
                id(),
                Severity.INFO,
                "Placeholder check executed",
                "This is a stub finding confirming the checks pipeline is wired correctly."));
    }
}
