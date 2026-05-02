package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

class PatientTest {

    @Test
    void testAddAndGetRecords() {
        Patient patient = new Patient(1);
        patient.addRecord(120.0, "SystolicPressure", 1000L);
        patient.addRecord(80.0, "DiastolicPressure", 2000L);
        patient.addRecord(98.0, "Saturation", 3000L);

        // Test normal range
        List<PatientRecord> records = patient.getRecords(1500L, 2500L);
        assertEquals(1, records.size());
        assertEquals(80.0, records.get(0).getMeasurementValue());

        // Test inclusive bounds
        List<PatientRecord> allRecords = patient.getRecords(1000L, 3000L);
        assertEquals(3, allRecords.size());

        // Test empty range
        List<PatientRecord> noRecords = patient.getRecords(4000L, 5000L);
        assertTrue(noRecords.isEmpty());
    }
}
