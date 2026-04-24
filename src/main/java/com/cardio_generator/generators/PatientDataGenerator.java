package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Represents a generic interface for generating various types of simulated
 * patient health data.
 * Classes implementing this interface define specific data generation logic
 * (e.g., ECG, blood pressure).
 */
public interface PatientDataGenerator {

    /**
     * Generates a single data point for a specified patient and delegates its
     * output
     * to the provided {@link OutputStrategy}.
     *
     * @param patientId      the unique identifier of the patient for whom data is
     *                       being generated.
     * @param outputStrategy the {@link OutputStrategy} defining how the generated
     *                       data should be handled or stored.
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
