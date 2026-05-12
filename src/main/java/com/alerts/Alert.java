package com.alerts;

/**
 * Represents an alert interface to support the Decorator pattern.
 */
public interface Alert {
    String getPatientId();
    String getCondition();
    long getTimestamp();
}
