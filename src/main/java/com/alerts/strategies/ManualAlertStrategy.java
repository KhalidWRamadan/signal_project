package com.alerts.strategies;

import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class ManualAlertStrategy implements AlertStrategy {

    @Override
    public Alert checkAlert(Patient patient) {
        List<PatientRecord> alertRecords = patient.getRecords(0, Long.MAX_VALUE).stream()
                .filter(r -> r.getRecordType().equals("Alert"))
                .sorted((r1, r2) -> Long.compare(r1.getTimestamp(), r2.getTimestamp()))
                .toList();

        if (alertRecords.isEmpty()) return null;

        PatientRecord lastRecord = alertRecords.get(alertRecords.size() - 1);
        
        // HealthDataSimulator outputs 1.0 for "triggered" and 0.0 for "resolved" (handled in our FileDataReader)
        if (lastRecord.getMeasurementValue() == 1.0) {
            return new Alert(String.valueOf(patient.getPatientId()), "Manual Alert Triggered", lastRecord.getTimestamp());
        }

        return null;
    }
}
