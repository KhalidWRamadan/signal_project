package com.cardio_generator.outputs;

/**
 * Defines the contract for outputting simulated patient health data.
 * Implementations of this interface handle how and where the generated data is
 * saved or transmitted
 * (e.g., printing to the console, saving to a file, or transmitting over a
 * network).
 */
public interface OutputStrategy {

    /**
     * Outputs a specific measurement or event for a patient.
     *
     * @param patientId the unique identifier of the patient associated with the
     *                  data.
     * @param timestamp the exact time the data was generated, in milliseconds since
     *                  the Unix epoch.
     * @param label     a string categorizing the type of data generated (e.g.,
     *                  "ECG", "BloodPressure").
     * @param data      the actual generated string representation of the data or
     *                  measurement value.
     */
    void output(int patientId, long timestamp, String label, String data);
}
