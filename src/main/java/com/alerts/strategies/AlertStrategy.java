package com.alerts.strategies;

import com.data_management.Patient;
import com.alerts.Alert;

/**
 * Interface defining the strategy for evaluating patient data and generating alerts.
 */
public interface AlertStrategy {
    
    /**
     * Evaluates the patient's data according to the implemented strategy.
     * If an alert condition is met, an Alert object should be returned.
     * 
     * @param patient the patient data to evaluate
     * @return an Alert if conditions are met, otherwise null
     */
    Alert checkAlert(Patient patient);
}
