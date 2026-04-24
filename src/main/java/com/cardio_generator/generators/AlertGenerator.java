package com.cardio_generator.generators;

// Reordered imports to be in ASCII sort order and removed blank line
import com.cardio_generator.outputs.OutputStrategy;
import java.util.Random;

/**
 * The {@code AlertGenerator} class is responsible for simulating spontaneous
 * medical alerts
 * for patients within the system. It periodically generates triggered and
 * resolved alert states
 * using basic probabilistic models, outputting these events via a specified
 * {@link OutputStrategy}.
 */
public class AlertGenerator implements PatientDataGenerator {

    /**
     * Shared random number generator utilized for determining alert probabilities.
     */
    public static final Random randomGenerator = new Random();

    // Changed variable name to camelCase
    /**
     * Tracks the current alert state for each patient by ID.
     * {@code false} indicates the alert is resolved or inactive. {@code true}
     * indicates an active alert.
     */
    private boolean[] alertStates; // false = resolved, true = pressed

    /**
     * Constructs a new {@code AlertGenerator} and initializes the alert states for
     * the given number of patients.
     * 
     * @param patientCount the total number of patients being monitored.
     */
    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

    /**
     * Simulates the alert lifecycle (triggering or resolving an alert) for a
     * specific patient
     * and dispatches the alert event to the provided output strategy.
     *
     * @param patientId      the unique integer identifier of the patient to
     *                       evaluate for an alert.
     * @param outputStrategy the {@link OutputStrategy} used to output the generated
     *                       alert data.
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                // Changed variable name to camelCase
                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = randomGenerator.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
