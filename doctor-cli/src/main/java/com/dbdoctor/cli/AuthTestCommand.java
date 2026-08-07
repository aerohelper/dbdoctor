package com.dbdoctor.cli;

import com.dbdoctor.api.DatabricksClient;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

/** Verifies that dbdoctor can authenticate to a Databricks workspace and reach its APIs. */
@Command(name = "test", description = "Test Databricks authentication and connectivity.")
public class AuthTestCommand implements Callable<Integer> {

    @Override
    public Integer call() {
        try {
            DatabricksClient client = new DatabricksClient();
            String workspace = client.workspaceUrl();
            String user = client.currentUserName();

            System.out.println("✓ Databricks authentication successful");
            System.out.println();
            System.out.println("Workspace: " + workspace);
            System.out.println("User: " + user);
            System.out.println("Status: CONNECTED");
            return 0;
        } catch (Exception e) {
            System.out.println("✗ Databricks authentication failed");
            System.out.println();
            System.out.println("Status: FAILED");
            System.out.println("Reason: " + e.getMessage());
            return 1;
        }
    }
}
