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

package com.dbdoctor.api;

import com.databricks.sdk.WorkspaceClient;
import com.databricks.sdk.service.iam.User;

/**
 * Wraps the Databricks SDK's {@link WorkspaceClient}, providing the operations
 * dbdoctor's checks and CLI commands need. Authentication is resolved by the SDK
 * itself from the environment (env vars, {@code ~/.databrickscfg} profile, etc.) —
 * see the Databricks CLI's {@code auth login} command for setting that up.
 */
public class DatabricksClient {

    private final WorkspaceClient workspaceClient;

    /** Creates a client using the SDK's default authentication resolution. */
    public DatabricksClient() {
        this(new WorkspaceClient());
    }

    public DatabricksClient(WorkspaceClient workspaceClient) {
        this.workspaceClient = workspaceClient;
    }

    /** The workspace URL this client is connected to. */
    public String workspaceUrl() {
        return workspaceClient.config().getHost();
    }

    /** The currently authenticated user's username (typically an email address). */
    public String currentUserName() {
        User user = workspaceClient.currentUser().me();
        return user.getUserName();
    }

    /** Access to the full underlying SDK client for capabilities not yet wrapped here. */
    public WorkspaceClient sdk() {
        return workspaceClient;
    }
}
