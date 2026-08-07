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
