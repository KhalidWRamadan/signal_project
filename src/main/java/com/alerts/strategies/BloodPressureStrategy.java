package com.alerts.strategies;

import com.alerts.Alert;
import com.alerts.factories.AlertFactory;
import com.alerts.factories.BloodPressureAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class BloodPressureStrategy implements AlertStrategy {

    private AlertFactory factory = new BloodPressureAlertFactory();

    @Override
    public Alert checkAlert(Patient patient) {
        List<PatientRecord> systolicRecords = patient.getRecords(0, Long.MAX_VALUE).stream()
                .filter(r -> r.getRecordType().equals("SystolicPressure"))
                .sorted((r1, r2) -> Long.compare(r1.getTimestamp(), r2.getTimestamp()))
                .toList();

        List<PatientRecord> diastolicRecords = patient.getRecords(0, Long.MAX_VALUE).stream()
                .filter(r -> r.getRecordType().equals("DiastolicPressure"))
                .sorted((r1, r2) -> Long.compare(r1.getTimestamp(), r2.getTimestamp()))
                .toList();

        // Check Critical Thresholds
        Alert criticalAlert = checkCriticalThresholds(systolicRecords, diastolicRecords, patient.getPatientId());
        if (criticalAlert != null) {
            return criticalAlert;
        }

        // Check Trends
        Alert trendAlert = checkTrends(systolicRecords, patient.getPatientId(), "SystolicPressure");
        if (trendAlert != null) return trendAlert;

        return checkTrends(diastolicRecords, patient.getPatientId(), "DiastolicPressure");
    }

    private Alert checkCriticalThresholds(List<PatientRecord> sysRecords, List<PatientRecord> diaRecords, int patientId) {
        if (!sysRecords.isEmpty()) {
            PatientRecord lastSys = sysRecords.get(sysRecords.size() - 1);
            if (lastSys.getMeasurementValue() > 180 || lastSys.getMeasurementValue() < 90) {
                return factory.createAlert(String.valueOf(patientId), "Critical Systolic Pressure", lastSys.getTimestamp());
            }
        }
        if (!diaRecords.isEmpty()) {
            PatientRecord lastDia = diaRecords.get(diaRecords.size() - 1);
            if (lastDia.getMeasurementValue() > 120 || lastDia.getMeasurementValue() < 60) {
                return factory.createAlert(String.valueOf(patientId), "Critical Diastolic Pressure", lastDia.getTimestamp());
            }
        }
        return null;
    }

    private Alert checkTrends(List<PatientRecord> records, int patientId, String type) {
        if (records.size() < 3) return null;

        int size = records.size();
        PatientRecord r1 = records.get(size - 3);
        PatientRecord r2 = records.get(size - 2);
        PatientRecord r3 = records.get(size - 1);

        double v1 = r1.getMeasurementValue();
        double v2 = r2.getMeasurementValue();
        double v3 = r3.getMeasurementValue();

        // Check increasing trend
        if ((v2 - v1 > 10) && (v3 - v2 > 10)) {
            return factory.createAlert(String.valueOf(patientId), "Increasing " + type + " Trend", r3.getTimestamp());
        }

        // Check decreasing trend
        if ((v1 - v2 > 10) && (v2 - v3 > 10)) {
            return factory.createAlert(String.valueOf(patientId), "Decreasing " + type + " Trend", r3.getTimestamp());
        }

        return null;
    }
}
