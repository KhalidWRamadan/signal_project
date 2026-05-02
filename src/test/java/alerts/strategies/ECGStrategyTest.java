package alerts.strategies;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.alerts.Alert;
import com.alerts.strategies.ECGStrategy;
import com.data_management.Patient;

class ECGStrategyTest {

    @Test
    void testAbnormalECGPike() {
        Patient patient = new Patient(1);
        
        // Window size is 10, so let's put 10 normal readings
        for (int i = 0; i < 10; i++) {
            patient.addRecord(1.0, "ECG", i * 1000L);
        }
        
        // Now add a peak (average is 1.0, threshold is > 1.5)
        patient.addRecord(1.6, "ECG", 11000L);

        ECGStrategy strategy = new ECGStrategy();
        Alert alert = strategy.checkAlert(patient);
        
        assertNotNull(alert);
        assertEquals("Abnormal ECG Data", alert.getCondition());
    }

    @Test
    void testNormalECG() {
        Patient patient = new Patient(1);
        
        for (int i = 0; i < 10; i++) {
            patient.addRecord(1.0, "ECG", i * 1000L);
        }
        
        patient.addRecord(1.2, "ECG", 11000L); // Not a massive deviation

        ECGStrategy strategy = new ECGStrategy();
        Alert alert = strategy.checkAlert(patient);
        
        assertNull(alert);
    }
}
