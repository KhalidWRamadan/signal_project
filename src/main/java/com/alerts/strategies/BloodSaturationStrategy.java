package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.factories.AlertFactory;
import com.alerts.factories.BloodOxygenAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class BloodSaturationStrategy implements AlertStrategy {

    private AlertFactory factory = new BloodOxygenAlertFactory();

    @Override
    public Alert checkAlert(Patient patient) {
        List<PatientRecord> saturationRecords = patient.getRecords(0, Long.MAX_VALUE).stream()
                .filter(r -> r.getRecordType().equals("Saturation"))
                .sorted((r1, r2) -> Long.compare(r1.getTimestamp(), r2.getTimestamp()))
                .toList();

        if (saturationRecords.isEmpty()) return null;

        // Check Low Saturation
        PatientRecord lastRecord = saturationRecords.get(saturationRecords.size() - 1);
        if (lastRecord.getMeasurementValue() < 92.0) {
            return factory.createAlert(String.valueOf(patient.getPatientId()), "Low Saturation", lastRecord.getTimestamp());
        }

        // Check Rapid Drop (5% or more within 10 minutes)
        long tenMinsInMillis = 10 * 60 * 1000;
        long currentTime = lastRecord.getTimestamp();

        for (int i = saturationRecords.size() - 1; i >= 0; i--) {
            PatientRecord historical = saturationRecords.get(i);
            if (currentTime - historical.getTimestamp() > tenMinsInMillis) {
                break; // Outside the 10-minute window
            }
            if (historical.getMeasurementValue() - lastRecord.getMeasurementValue() >= 5.0) {
                return factory.createAlert(String.valueOf(patient.getPatientId()), "Rapid Saturation Drop", currentTime);
            }
        }

        return null;
    }
}
