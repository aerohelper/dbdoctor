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

package com.dbdoctor.checks.cluster;

import com.dbdoctor.checks.HealthCheck;
import com.dbdoctor.core.model.CheckResult;
import com.dbdoctor.core.model.Severity;
import com.dbdoctor.core.model.WorkspaceSnapshot;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * CLUSTER-003 — flags clusters running a Databricks Runtime major version older than a
 * configurable minimum. Runtime version strings look like {@code "13.3.x-scala2.12"};
 * this check parses the leading major version number and ignores anything it can't parse
 * (custom/unknown runtimes are not treated as violations).
 *
 * <p>The default minimum (major version 13) is dbdoctor's own opinion, not a documented
 * Databricks end-of-support date — pass a different value to the constructor to change it.
 */
public class OutdatedRuntimeCheck implements HealthCheck {

    private static final int DEFAULT_MIN_MAJOR_VERSION = 13;
    private static final Pattern MAJOR_VERSION_PATTERN = Pattern.compile("^(\\d+)\\.");

    private final int minSupportedMajorVersion;

    public OutdatedRuntimeCheck() {
        this(DEFAULT_MIN_MAJOR_VERSION);
    }

    public OutdatedRuntimeCheck(int minSupportedMajorVersion) {
        this.minSupportedMajorVersion = minSupportedMajorVersion;
    }

    @Override
    public CheckResult execute(WorkspaceSnapshot workspace) {
        List<String> offenders = workspace.clusters().stream()
                .filter(c -> isOutdated(c.runtimeVersion()))
                .map(c -> c.name() + " (" + c.runtimeVersion() + ")")
                .collect(Collectors.toList());

        if (offenders.isEmpty()) {
            return new CheckResult("CLUSTER-003", "Runtime versions are current", Severity.PASS,
                    "No cluster is running a Databricks Runtime older than major version "
                            + minSupportedMajorVersion + ".", "N/A");
        }

        return new CheckResult("CLUSTER-003", "Outdated Databricks Runtime", Severity.WARNING,
                "These clusters are running a Databricks Runtime older than major version "
                        + minSupportedMajorVersion + ": " + String.join(", ", offenders) + ".",
                "Upgrade to a supported, current Databricks Runtime version.");
    }

    private boolean isOutdated(String runtimeVersion) {
        if (runtimeVersion == null) {
            return false;
        }
        Matcher matcher = MAJOR_VERSION_PATTERN.matcher(runtimeVersion);
        if (!matcher.find()) {
            return false;
        }
        int majorVersion = Integer.parseInt(matcher.group(1));
        return majorVersion < minSupportedMajorVersion;
    }
}
