package com.dbdoctor.core.score;

import com.dbdoctor.core.model.CheckResult;
import com.dbdoctor.core.model.HealthScore;
import com.dbdoctor.core.model.Severity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreCalculatorTest {

    private final ScoreCalculator calculator = new ScoreCalculator();

    private static CheckResult result(Severity severity) {
        return new CheckResult("X-1", "title", severity, "description", "recommendation");
    }

    @Test
    void allPassingScoresOneHundred() {
        HealthScore score = calculator.calculate(List.of(result(Severity.PASS), result(Severity.PASS)));

        assertEquals(100, score.score());
        assertEquals(2, score.passed());
        assertEquals(2, score.total());
    }

    @Test
    void deductsFifteenPerCritical() {
        HealthScore score = calculator.calculate(List.of(result(Severity.CRITICAL)));

        assertEquals(85, score.score());
        assertEquals(1, score.criticals());
    }

    @Test
    void deductsFivePerWarning() {
        HealthScore score = calculator.calculate(List.of(result(Severity.WARNING)));

        assertEquals(95, score.score());
        assertEquals(1, score.warnings());
    }

    @Test
    void infoDoesNotAffectScore() {
        HealthScore score = calculator.calculate(List.of(result(Severity.INFO)));

        assertEquals(100, score.score());
        assertEquals(1, score.info());
    }

    @Test
    void scoreNeverGoesBelowZero() {
        HealthScore score = calculator.calculate(List.of(
                result(Severity.CRITICAL), result(Severity.CRITICAL), result(Severity.CRITICAL),
                result(Severity.CRITICAL), result(Severity.CRITICAL), result(Severity.CRITICAL),
                result(Severity.CRITICAL), result(Severity.CRITICAL)
        ));

        assertEquals(0, score.score());
    }

    @Test
    void customPenaltiesAreRespected() {
        ScoreCalculator custom = new ScoreCalculator(100, 50, 10, 0);
        HealthScore score = custom.calculate(List.of(result(Severity.CRITICAL)));

        assertEquals(50, score.score());
    }
}
