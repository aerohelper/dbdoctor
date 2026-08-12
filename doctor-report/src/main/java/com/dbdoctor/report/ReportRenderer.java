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

package com.dbdoctor.report;

import com.dbdoctor.core.model.HealthScore;
import com.dbdoctor.core.model.ScanReport;

/** Renders a {@link ScanReport} and its {@link HealthScore} into a specific output format. */
public interface ReportRenderer {

    /** Renders the report as a string. Callers decide whether to print it or write it to a file. */
    String render(ScanReport report, HealthScore score);
}
