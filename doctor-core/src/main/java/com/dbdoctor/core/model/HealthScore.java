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
 * A summary score derived from a set of {@link CheckResult}s. This is dbdoctor's own opinionated
 * score, not an industry-standard metric.
 *
 * @param score     0-100, starting at 100 and deducted per finding (never below 0)
 * @param total     total number of checks that ran
 * @param passed    number of checks with severity PASS
 * @param info      number of checks with severity INFO
 * @param warnings  number of checks with severity WARNING
 * @param criticals number of checks with severity CRITICAL
 */
public record HealthScore(int score, int total, int passed, int info, int warnings, int criticals) {
}
