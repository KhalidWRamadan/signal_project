package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.alerts.strategies.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    private List<AlertStrategy> strategies;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.strategies = new ArrayList<>();
        this.strategies.add(new BloodPressureStrategy());
        this.strategies.add(new BloodSaturationStrategy());
        this.strategies.add(new HypotensiveHypoxemiaStrategy());
        this.strategies.add(new ECGStrategy());
        this.strategies.add(new ManualAlertStrategy());
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method iterates over defined alert strategies.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        for (AlertStrategy strategy : strategies) {
            Alert alert = strategy.checkAlert(patient);
            if (alert != null) {
                triggerAlert(alert);
            }
        }
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        System.out.println("ALERT TRIGGERED: Patient " + alert.getPatientId() + 
                           " | Condition: " + alert.getCondition() + 
                           " | Timestamp: " + alert.getTimestamp());
    }
}
