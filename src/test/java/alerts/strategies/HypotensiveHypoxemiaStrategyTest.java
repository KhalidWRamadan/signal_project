package alerts.strategies;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.alerts.Alert;
import com.alerts.strategies.HypotensiveHypoxemiaStrategy;
import com.data_management.Patient;

class HypotensiveHypoxemiaStrategyTest {

    @Test
    void testHypotensiveHypoxemiaTrigger() {
        Patient patient = new Patient(1);
        patient.addRecord(85.0, "SystolicPressure", 1000L); // Critical low Sys (<90)
        patient.addRecord(90.0, "Saturation", 2000L); // Critical low Sat (<92)

        HypotensiveHypoxemiaStrategy strategy = new HypotensiveHypoxemiaStrategy();
        Alert alert = strategy.checkAlert(patient);
        
        assertNotNull(alert);
        assertEquals("Hypotensive Hypoxemia", alert.getCondition());
    }

    @Test
    void testHypotensiveHypoxemiaNoTrigger() {
        Patient patient = new Patient(1);
        patient.addRecord(85.0, "SystolicPressure", 1000L); // Low Sys
        patient.addRecord(95.0, "Saturation", 2000L); // Normal Sat

        HypotensiveHypoxemiaStrategy strategy = new HypotensiveHypoxemiaStrategy();
        Alert alert = strategy.checkAlert(patient);
        
        assertNull(alert);
    }
}
