package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@code FileOutputStrategy} class implements the {@link OutputStrategy}
 * interface
 * to write generated patient health data into local text files.
 * Files are organized by data label within a specified base directory.
 */
public class FileOutputStrategy implements OutputStrategy {

    // Changed variable name to camelCase
    private String baseDirectory;

    /**
     * A thread-safe map caching the absolute file paths associated with each data
     * label.
     */
    // Changed variable name to camelCase
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    /**
     * Constructs a new {@code FileOutputStrategy} targeting the specified base
     * directory.
     *
     * @param baseDirectory the relative or absolute path to the directory where
     *                      data files
     *                      will be created and written to.
     */
    public FileOutputStrategy(String baseDirectory) {

        this.baseDirectory = baseDirectory;
    }

    /**
     * Outputs the specified patient data by appending it to a file corresponding to
     * the data label.
     * If the base directory or the specific file does not exist, they are created
     * automatically.
     *
     * @param patientId the unique identifier of the patient associated with the
     *                  data.
     * @param timestamp the exact time the data was generated, in milliseconds since
     *                  the Unix epoch.
     * @param label     a string categorizing the type of data (used to name the
     *                  output file).
     * @param data      the string representation of the generated data or
     *                  measurement value.
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the filePath variable
        // Changed variable name to camelCase
        String filePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}
