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

package com.dbdoctor.core.config;

/**
 * Root configuration for dbdoctor's checks, loaded from a YAML file (e.g. {@code dbdoctor.yml}).
 * Every field has a default matching the check's own built-in default, so a partial YAML file
 * (or none at all) is fine — only the fields you set are overridden.
 */
public class DoctorConfig {

    public ChecksConfig checks = new ChecksConfig();

    public static class ChecksConfig {
        public ClusterConfig cluster = new ClusterConfig();
        public JobConfig job = new JobConfig();
        public WarehouseConfig warehouse = new WarehouseConfig();
    }

    public static class ClusterConfig {
        /** CLUSTER-002: flags clusters whose auto-termination timeout exceeds this. */
        public int maxAutoTerminationMinutes = 120;
        /** CLUSTER-003: flags clusters on a runtime older than this major version. */
        public int minSupportedRuntimeMajorVersion = 13;
        /** CLUSTER-005: flags fixed-size clusters with more workers than this. */
        public int maxWorkers = 10;
    }

    public static class JobConfig {
        /** JOB-002: flags jobs with at least this many failures among their recent runs. */
        public int failureThreshold = 3;
        /** JOB-003: flags jobs whose most recent run exceeded this duration. */
        public long maxRuntimeMinutes = 180;
    }

    public static class WarehouseConfig {
        /** SQL-002: warehouse size tiers considered "oversized". */
        public java.util.Set<String> largeSizes = java.util.Set.of(
                "Large", "X-Large", "2X-Large", "3X-Large", "4X-Large");
    }
}
