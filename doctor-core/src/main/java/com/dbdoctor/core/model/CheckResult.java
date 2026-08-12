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

package com.dbdoctor.core.model;

/**
 * The outcome of running a single health check.
 *
 * @param checkId        stable identifier of the check, e.g. {@code CLUSTER-001}
 * @param title          short human-readable summary of the finding
 * @param severity       how serious the finding is
 * @param description    detailed explanation of what was found and why it matters
 * @param recommendation actionable guidance on how to resolve the issue
 */
public record CheckResult(
        String checkId,
        String title,
        Severity severity,
        String description,
        String recommendation
) {
}
