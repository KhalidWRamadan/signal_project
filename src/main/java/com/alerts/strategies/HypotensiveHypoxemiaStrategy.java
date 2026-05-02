package com.alerts.strategies;

import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class HypotensiveHypoxemiaStrategy implements AlertStrategy {

    @Override
    public Alert checkAlert(Patient patient) {
        List<PatientRecord> systolicRecords = patient.getRecords(0, Long.MAX_VALUE).stream()
                .filter(r -> r.getRecordType().equals("SystolicPressure"))
                .sorted((r1, r2) -> Long.compare(r1.getTimestamp(), r2.getTimestamp()))
                .toList();

        List<PatientRecord> saturationRecords = patient.getRecords(0, Long.MAX_VALUE).stream()
                .filter(r -> r.getRecordType().equals("Saturation"))
                .sorted((r1, r2) -> Long.compare(r1.getTimestamp(), r2.getTimestamp()))
                .toList();

        if (systolicRecords.isEmpty() || saturationRecords.isEmpty()) {
            return null;
        }

        PatientRecord lastSys = systolicRecords.get(systolicRecords.size() - 1);
        PatientRecord lastSat = saturationRecords.get(saturationRecords.size() - 1);

        // Check if both readings are critical and roughly taken around the same time
        if (lastSys.getMeasurementValue() < 90.0 && lastSat.getMeasurementValue() < 92.0) {
            long maxTime = Math.max(lastSys.getTimestamp(), lastSat.getTimestamp());
            return new Alert(String.valueOf(patient.getPatientId()), "Hypotensive Hypoxemia", maxTime);
        }

        return null;
    }
}
