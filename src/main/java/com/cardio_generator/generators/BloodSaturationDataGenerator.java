package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * The {@code BloodSaturationDataGenerator} class generates simulated blood
 * oxygen saturation
 * (SpO2) data for patients.
 * It implements the {@link PatientDataGenerator} interface to periodically
 * generate and output
 * these fluctuations within a realistic medical range (90% to 100%).
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {
    private static final Random random = new Random();
    private int[] lastSaturationValues;

    /**
     * Constructs a {@code BloodSaturationDataGenerator} and initializes baseline
     * saturation values for each patient.
     * 
     * @param patientCount the total number of patients to initialize data for.
     */
    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1];

        // Initialize with baseline saturation values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Initializes with a value between 95 and 100
        }
    }

    /**
     * Generates a new blood saturation value for the specified patient and passes
     * it to the chosen output strategy.
     * The generated value is calculated by applying a minor random fluctuation
     * (-1%, 0%, or +1%) to the patient's
     * previous saturation value, strictly keeping the final output bounded between
     * 90% and 100%.
     *
     * @param patientId      the unique integer identifier of the patient.
     * @param outputStrategy the {@link OutputStrategy} used to output the generated
     *                       data.
     *                       Any exceptions encountered during generation will be
     *                       caught and logged.
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Simulate blood saturation values
            int variation = random.nextInt(3) - 1; // -1, 0, or 1 to simulate small fluctuations
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Ensure the saturation stays within a realistic and healthy range
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
                    Double.toString(newSaturationValue) + "%");
        } catch (Exception e) {
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace(); // This will print the stack trace to help identify where the error occurred.
        }
    }
}
