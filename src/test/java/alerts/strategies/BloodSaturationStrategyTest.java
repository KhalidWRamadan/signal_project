package alerts.strategies;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.alerts.Alert;
import com.alerts.strategies.BloodSaturationStrategy;
import com.data_management.Patient;

class BloodSaturationStrategyTest {

    @Test
    void testLowSaturation() {
        Patient patient = new Patient(1);
        patient.addRecord(91.0, "Saturation", 1000L);
        
        BloodSaturationStrategy strategy = new BloodSaturationStrategy();
        Alert alert = strategy.checkAlert(patient);
        
        assertNotNull(alert);
        assertEquals("Low Saturation", alert.getCondition());
    }

    @Test
    void testRapidDrop() {
        Patient patient = new Patient(1);
        patient.addRecord(99.0, "Saturation", 1000L); // T0
        // Rapid drop of 5% within 10 minutes (600,000 ms)
        patient.addRecord(94.0, "Saturation", 5 * 60 * 1000L); 
        
        BloodSaturationStrategy strategy = new BloodSaturationStrategy();
        Alert alert = strategy.checkAlert(patient);
        
        assertNotNull(alert);
        assertEquals("Rapid Saturation Drop", alert.getCondition());
    }
    
    @Test
    void testNoRapidDropOverLongTime() {
        Patient patient = new Patient(1);
        patient.addRecord(99.0, "Saturation", 1000L); 
        // Drop of 5% but takes 11 minutes (660,000 ms)
        patient.addRecord(94.0, "Saturation", 11 * 60 * 1000L); 
        
        BloodSaturationStrategy strategy = new BloodSaturationStrategy();
        Alert alert = strategy.checkAlert(patient);
        
        assertNull(alert); // Shouldn't trigger because it's outside the 10 min window
    }

    @Test
    void testNormalSaturation() {
        Patient patient = new Patient(1);
        patient.addRecord(98.0, "Saturation", 1000L);
        
        BloodSaturationStrategy strategy = new BloodSaturationStrategy();
        Alert alert = strategy.checkAlert(patient);
        
        assertNull(alert);
    }
}
