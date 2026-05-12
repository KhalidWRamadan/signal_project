package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import com.data_management.PatientRecord;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

class FileDataReaderTest {

    @Test
    void testReadDataFromFile() throws IOException {
        DataStorage storage = DataStorage.getInstance();
        
        // Create a temporary test directory
        File tempDir = new File("test_output");
        tempDir.mkdir();

        // Create a mock output file
        File testFile = new File(tempDir, "mock_data.txt");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("Patient ID: 1, Timestamp: 1000, Label: SystolicPressure, Data: 120.5\n");
            writer.write("Patient ID: 1, Timestamp: 2000, Label: Alert, Data: triggered\n");
            writer.write("Patient ID: 2, Timestamp: 3000, Label: Saturation, Data: 98.0%\n"); // Data string might have % or not, regex clears it
            writer.write("Invalid format line here\n"); // Edge case
        }

        FileDataReader reader = new FileDataReader("test_output");
        reader.readData(storage);

        // Verify patient 1
        List<PatientRecord> p1Records = storage.getRecords(1, 0, 5000);
        assertEquals(2, p1Records.size());
        assertEquals(120.5, p1Records.get(0).getMeasurementValue());
        assertEquals(1.0, p1Records.get(1).getMeasurementValue()); // Triggered alert becomes 1.0

        // Verify patient 2
        List<PatientRecord> p2Records = storage.getRecords(2, 0, 5000);
        assertEquals(1, p2Records.size());
        assertEquals(98.0, p2Records.get(0).getMeasurementValue());

        // Cleanup
        testFile.delete();
        tempDir.delete();
    }
    
    @Test
    void testDirectoryDoesNotExist() {
        FileDataReader reader = new FileDataReader("non_existent_directory_for_test");
        assertThrows(IOException.class, () -> {
            reader.readData(DataStorage.getInstance());
        });
    }
}
