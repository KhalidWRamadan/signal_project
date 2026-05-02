package alerts.strategies;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.alerts.Alert;
import com.alerts.strategies.BloodPressureStrategy;
import com.data_management.Patient;

class BloodPressureStrategyTest {

    @Test
    void testCriticalSystolicHigh() {
        Patient patient = new Patient(1);
        patient.addRecord(185.0, "SystolicPressure", 1000L);
        BloodPressureStrategy strategy = new BloodPressureStrategy();
        Alert alert = strategy.checkAlert(patient);
        assertNotNull(alert);
        assertEquals("Critical Systolic Pressure", alert.getCondition());
    }

    @Test
    void testCriticalDiastolicLow() {
        Patient patient = new Patient(1);
        patient.addRecord(55.0, "DiastolicPressure", 1000L);
        BloodPressureStrategy strategy = new BloodPressureStrategy();
        Alert alert = strategy.checkAlert(patient);
        assertNotNull(alert);
        assertEquals("Critical Diastolic Pressure", alert.getCondition());
    }

    @Test
    void testIncreasingTrend() {
        Patient patient = new Patient(1);
        patient.addRecord(110.0, "SystolicPressure", 1000L);
        patient.addRecord(121.0, "SystolicPressure", 2000L);
        patient.addRecord(132.0, "SystolicPressure", 3000L);

        BloodPressureStrategy strategy = new BloodPressureStrategy();
        Alert alert = strategy.checkAlert(patient);
        assertNotNull(alert);
        assertEquals("Increasing SystolicPressure Trend", alert.getCondition());
    }

    @Test
    void testDecreasingTrend() {
        Patient patient = new Patient(1);
        patient.addRecord(150.0, "DiastolicPressure", 1000L);
        patient.addRecord(139.0, "DiastolicPressure", 2000L);
        patient.addRecord(128.0, "DiastolicPressure", 3000L); // Note: 128 is > 120 so it's a critical diastolic! Let's use valid trend values within 60-120 range.
        
        // Use values that don't overlap with critical thresholds
        Patient patient2 = new Patient(2);
        patient2.addRecord(110.0, "DiastolicPressure", 1000L);
        patient2.addRecord(95.0, "DiastolicPressure", 2000L);
        patient2.addRecord(80.0, "DiastolicPressure", 3000L);

        BloodPressureStrategy strategy = new BloodPressureStrategy();
        Alert alert = strategy.checkAlert(patient2);
        assertNotNull(alert);
        assertEquals("Decreasing DiastolicPressure Trend", alert.getCondition());
    }

    @Test
    void testNormalPressure() {
        Patient patient = new Patient(1);
        patient.addRecord(120.0, "SystolicPressure", 1000L);
        patient.addRecord(80.0, "DiastolicPressure", 2000L);

        BloodPressureStrategy strategy = new BloodPressureStrategy();
        Alert alert = strategy.checkAlert(patient);
        assertNull(alert);
    }
}
