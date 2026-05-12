package alerts.factories;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.alerts.Alert;
import com.alerts.BloodOxygenAlert;
import com.alerts.BloodPressureAlert;
import com.alerts.ECGAlert;
import com.alerts.factories.AlertFactory;
import com.alerts.factories.BloodOxygenAlertFactory;
import com.alerts.factories.BloodPressureAlertFactory;
import com.alerts.factories.ECGAlertFactory;

class AlertFactoryTest {

    @Test
    void testBloodPressureFactory() {
        AlertFactory factory = new BloodPressureAlertFactory();
        Alert alert = factory.createAlert("1", "High BP", 1000L);
        
        assertNotNull(alert);
        assertTrue(alert instanceof BloodPressureAlert);
        assertEquals("1", alert.getPatientId());
        assertEquals("High BP", alert.getCondition());
        assertEquals(1000L, alert.getTimestamp());
    }

    @Test
    void testBloodOxygenFactory() {
        AlertFactory factory = new BloodOxygenAlertFactory();
        Alert alert = factory.createAlert("2", "Low O2", 2000L);
        
        assertNotNull(alert);
        assertTrue(alert instanceof BloodOxygenAlert);
        assertEquals("2", alert.getPatientId());
        assertEquals("Low O2", alert.getCondition());
    }

    @Test
    void testECGFactory() {
        AlertFactory factory = new ECGAlertFactory();
        Alert alert = factory.createAlert("3", "Arrhythmia", 3000L);
        
        assertNotNull(alert);
        assertTrue(alert instanceof ECGAlert);
        assertEquals("3", alert.getPatientId());
        assertEquals("Arrhythmia", alert.getCondition());
    }
}
