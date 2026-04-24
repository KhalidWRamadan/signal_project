package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * The {@code TcpOutputStrategy} class implements the {@link OutputStrategy}
 * interface
 * to stream simulated patient health data to a connected client over a TCP
 * socket.
 * Note: Currently this implementation handles a single client connection at a
 * time.
 */
public class TcpOutputStrategy implements OutputStrategy {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;

    /**
     * Constructs a new {@code TcpOutputStrategy} that opens a server socket on the
     * specified port.
     * It waits for a client connection in a separate background thread to avoid
     * blocking the
     * main simulation thread.
     *
     * @param port the local port number on which the TCP server will listen for
     *             incoming
     *             client connections.
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Outputs the specified patient data by transmitting it over the TCP socket as
     * a formatted CSV string.
     * If no client is connected, the data is silently ignored.
     *
     * @param patientId the unique identifier of the patient associated with the
     *                  data.
     * @param timestamp the exact time the data was generated, in milliseconds since
     *                  the Unix epoch.
     * @param label     a string categorizing the type of data generated (e.g.,
     *                  "ECG").
     * @param data      the actual generated string representation of the data.
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
