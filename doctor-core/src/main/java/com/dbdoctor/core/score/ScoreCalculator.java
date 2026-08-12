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

package com.dbdoctor.core.score;

import com.dbdoctor.core.model.CheckResult;
import com.dbdoctor.core.model.HealthScore;
import com.dbdoctor.core.model.Severity;

import java.util.List;

/**
 * Computes dbdoctor's own health score from a set of check results: starts at 100 points and
 * deducts a configurable penalty per CRITICAL/WARNING finding, floored at 0. This is
 * deliberately a simple, transparent algorithm — not an industry-standard metric, and not
 * a claim that severity-weighted subtraction is the "correct" way to score a workspace.
 */
public class ScoreCalculator {

    private static final int DEFAULT_MAX_SCORE = 100;
    private static final int DEFAULT_CRITICAL_PENALTY = 15;
    private static final int DEFAULT_WARNING_PENALTY = 5;
    private static final int DEFAULT_INFO_PENALTY = 0;

    private final int maxScore;
    private final int criticalPenalty;
    private final int warningPenalty;
    private final int infoPenalty;

    public ScoreCalculator() {
        this(DEFAULT_MAX_SCORE, DEFAULT_CRITICAL_PENALTY, DEFAULT_WARNING_PENALTY, DEFAULT_INFO_PENALTY);
    }

    public ScoreCalculator(int maxScore, int criticalPenalty, int warningPenalty, int infoPenalty) {
        this.maxScore = maxScore;
        this.criticalPenalty = criticalPenalty;
        this.warningPenalty = warningPenalty;
        this.infoPenalty = infoPenalty;
    }

    public HealthScore calculate(List<CheckResult> results) {
        int passed = 0;
        int info = 0;
        int warnings = 0;
        int criticals = 0;
        int deductions = 0;

        for (CheckResult result : results) {
            switch (result.severity()) {
                case PASS -> passed++;
                case INFO -> {
                    info++;
                    deductions += infoPenalty;
                }
                case WARNING -> {
                    warnings++;
                    deductions += warningPenalty;
                }
                case CRITICAL -> {
                    criticals++;
                    deductions += criticalPenalty;
                }
            }
        }

        int score = Math.max(0, maxScore - deductions);
        return new HealthScore(score, results.size(), passed, info, warnings, criticals);
    }
}
