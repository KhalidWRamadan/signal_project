package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.factories.AlertFactory;
import com.alerts.factories.ECGAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class ECGStrategy implements AlertStrategy {

    private AlertFactory factory = new ECGAlertFactory();

    @Override
    public Alert checkAlert(Patient patient) {
        List<PatientRecord> ecgRecords = patient.getRecords(0, Long.MAX_VALUE).stream()
                .filter(r -> r.getRecordType().equals("ECG"))
                .sorted((r1, r2) -> Long.compare(r1.getTimestamp(), r2.getTimestamp()))
                .toList();

        if (ecgRecords.isEmpty()) return null;

        // Sliding window approach: Let's consider the last 10 readings for an average
        int windowSize = 10;
        int startIndex = Math.max(0, ecgRecords.size() - windowSize);
        
        double sum = 0;
        int count = 0;
        for (int i = startIndex; i < ecgRecords.size(); i++) {
            sum += ecgRecords.get(i).getMeasurementValue();
            count++;
        }
        
        if (count == 0) return null;
        double average = sum / count;

        PatientRecord lastRecord = ecgRecords.get(ecgRecords.size() - 1);
        
        // Let's define "far beyond" as a 50% deviation from the sliding window average
        if (Math.abs(lastRecord.getMeasurementValue() - average) > (0.5 * average)) {
             return factory.createAlert(String.valueOf(patient.getPatientId()), "Abnormal ECG Data", lastRecord.getTimestamp());
        }

        return null;
    }
}
