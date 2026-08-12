/*
 * Copyright 2026 Databricks Doctor contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
