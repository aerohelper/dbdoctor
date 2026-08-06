package com.dbdoctor.checks;

import com.dbdoctor.api.DatabricksApiClient;
import com.dbdoctor.core.model.Finding;
import com.dbdoctor.core.model.Severity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class PlaceholderCheckTest {

    @Mock
    private DatabricksApiClient client;

    @Test
    void runReturnsOneInfoFinding() {
        PlaceholderCheck check = new PlaceholderCheck();

        List<Finding> findings = check.run(client);

        assertFalse(findings.isEmpty());
        assertEquals(Severity.INFO, findings.get(0).severity());
        assertEquals("placeholder-check", findings.get(0).checkId());
    }

    @Test
    void idAndNameAreStable() {
        PlaceholderCheck check = new PlaceholderCheck();

        assertEquals("placeholder-check", check.id());
        assertEquals("Placeholder Check", check.name());
    }
}
