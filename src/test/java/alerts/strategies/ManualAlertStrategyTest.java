package alerts.strategies;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.alerts.Alert;
import com.alerts.strategies.ManualAlertStrategy;
import com.data_management.Patient;

class ManualAlertStrategyTest {

    @Test
    void testManualAlertTriggered() {
        Patient patient = new Patient(1);
        patient.addRecord(1.0, "Alert", 1000L); // 1.0 means triggered

        ManualAlertStrategy strategy = new ManualAlertStrategy();
        Alert alert = strategy.checkAlert(patient);
        
        assertNotNull(alert);
        assertEquals("Manual Alert Triggered", alert.getCondition());
    }

    @Test
    void testManualAlertResolved() {
        Patient patient = new Patient(1);
        patient.addRecord(1.0, "Alert", 1000L);
        patient.addRecord(0.0, "Alert", 2000L); // 0.0 means resolved

        ManualAlertStrategy strategy = new ManualAlertStrategy();
        Alert alert = strategy.checkAlert(patient);
        
        assertNull(alert);
    }
}
