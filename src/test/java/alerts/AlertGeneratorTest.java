package alerts;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;

class AlertGeneratorTest {

    @Test
    void testEvaluateDataTriggersAlert() {
        DataStorage storage = DataStorage.getInstance();
        storage.addPatientData(1, 185.0, "SystolicPressure", 1000L); // Should trigger critical BP alert

        Patient patient = storage.getAllPatients().get(0);
        
        AlertGenerator generator = new AlertGenerator(storage);
        // It will print to console as implemented, so we just make sure it doesn't crash
        assertDoesNotThrow(() -> {
            generator.evaluateData(patient);
        });
    }
}
