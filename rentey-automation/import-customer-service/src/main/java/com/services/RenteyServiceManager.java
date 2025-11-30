package com.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * Service to manage the rentey-service lifecycle.
 * Automatically starts rentey-service if it's not running when needed.
 */
@Service
public class RenteyServiceManager {

    private static final Logger logger = LoggerFactory.getLogger(RenteyServiceManager.class);
    
    private final String renteyServiceBaseUrl;
    private final int renteyServicePort;
    private Process renteyServiceProcess;
    private final String projectRootPath;

    public RenteyServiceManager(
            @Value("${rentey.service.base-url}") String renteyServiceBaseUrl) {
        this.renteyServiceBaseUrl = renteyServiceBaseUrl;
        // Extract port from URL (e.g., http://localhost:8089 -> 8089)
        this.renteyServicePort = extractPort(renteyServiceBaseUrl);
        // Get project root - try to find it relative to current working directory
        this.projectRootPath = findProjectRoot();
    }

    /**
     * Finds the project root directory by looking for rentey-service directory.
     */
    private String findProjectRoot() {
        String currentDir = System.getProperty("user.dir");
        Path currentPath = Paths.get(currentDir);
        
        // Check if we're already at project root (rentey-service exists here)
        Path renteyServicePath = currentPath.resolve("rentey-service");
        if (Files.exists(renteyServicePath)) {
            logger.debug("Found project root at: {}", currentPath);
            return currentDir;
        }
        
        // Check if we're in import-customer-service, go up one level
        if (currentPath.getFileName().toString().equals("import-customer-service")) {
            Path parentPath = currentPath.getParent();
            if (Files.exists(parentPath.resolve("rentey-service"))) {
                logger.debug("Found project root at: {}", parentPath);
                return parentPath.toString();
            }
        }
        
        // Try going up from current directory
        Path searchPath = currentPath;
        for (int i = 0; i < 5; i++) { // Search up to 5 levels
            if (Files.exists(searchPath.resolve("rentey-service"))) {
                logger.debug("Found project root at: {}", searchPath);
                return searchPath.toString();
            }
            Path parent = searchPath.getParent();
            if (parent == null) {
                break;
            }
            searchPath = parent;
        }
        
        // Fallback to current directory
        logger.warn("Could not find project root, using current directory: {}", currentDir);
        return currentDir;
    }

    /**
     * Ensures rentey-service is running. If not, starts it automatically.
     */
    public void ensureRenteyServiceIsRunning() {
        if (isRenteyServiceRunning()) {
            logger.info("rentey-service is already running on port {}", renteyServicePort);
            return;
        }

        logger.info("rentey-service is not running. Attempting to start it automatically...");
        startRenteyService();
        waitForServiceToBeReady();
    }

    /**
     * Checks if rentey-service is running by attempting to connect to its port.
     */
    private boolean isRenteyServiceRunning() {
        try (Socket socket = new Socket()) {
            socket.connect(new java.net.InetSocketAddress("localhost", renteyServicePort), 1000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Starts rentey-service using Maven.
     */
    private void startRenteyService() {
        try {
            Path projectRoot = Paths.get(projectRootPath);
            Path renteyServicePath = projectRoot.resolve("rentey-service");
            
            if (!Files.exists(renteyServicePath)) {
                logger.error("rentey-service directory not found at: {}", renteyServicePath);
                throw new RuntimeException("rentey-service directory not found at: " + renteyServicePath);
            }

            // Determine Maven wrapper command based on OS
            String mavenCommand = isWindows() ? "mvnw.cmd" : "mvnw";
            Path mavenWrapper = renteyServicePath.resolve(mavenCommand);
            
            // If mvnw doesn't exist in rentey-service, check project root
            if (!Files.exists(mavenWrapper)) {
                mavenWrapper = projectRoot.resolve(mavenCommand);
            }
            
            // If still not found, try using system mvn
            String command;
            if (Files.exists(mavenWrapper)) {
                command = mavenWrapper.toAbsolutePath().toString();
                if (!isWindows()) {
                    // Make sure it's executable on Unix-like systems
                    try {
                        Runtime.getRuntime().exec("chmod +x " + command);
                    } catch (Exception e) {
                        logger.warn("Could not make mvnw executable: {}", e.getMessage());
                    }
                }
            } else {
                command = "mvn";
                logger.warn("Maven wrapper not found, using system mvn command");
            }

            // Build the command to start Spring Boot application
            ProcessBuilder processBuilder = new ProcessBuilder(
                    command,
                    "spring-boot:run"
            );
            
            processBuilder.directory(renteyServicePath.toFile());
            processBuilder.redirectErrorStream(true);
            
            // Set environment variables if needed
            processBuilder.environment().put("MAVEN_OPTS", "-Xmx512m");
            
            // Start the process
            renteyServiceProcess = processBuilder.start();
            
            logger.info("Started rentey-service process (PID: {}) from directory: {}", 
                    renteyServiceProcess.pid(), renteyServicePath);
            
            // Add shutdown hook to cleanup process when application stops
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (renteyServiceProcess != null && renteyServiceProcess.isAlive()) {
                    logger.info("Stopping rentey-service process...");
                    renteyServiceProcess.destroy();
                    try {
                        if (!renteyServiceProcess.waitFor(10, TimeUnit.SECONDS)) {
                            renteyServiceProcess.destroyForcibly();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        renteyServiceProcess.destroyForcibly();
                    }
                }
            }));
            
        } catch (Exception e) {
            logger.error("Failed to start rentey-service: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to start rentey-service automatically", e);
        }
    }

    /**
     * Waits for rentey-service to be ready by checking if the port is open.
     */
    private void waitForServiceToBeReady() {
        int maxAttempts = 60; // Wait up to 60 seconds
        int attempt = 0;
        
        logger.info("Waiting for rentey-service to be ready...");
        
        while (attempt < maxAttempts) {
            if (isRenteyServiceRunning()) {
                logger.info("rentey-service is now ready!");
                return;
            }
            
            try {
                Thread.sleep(1000); // Wait 1 second between attempts
                attempt++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for rentey-service", e);
            }
        }
        
        throw new RuntimeException("rentey-service did not become ready within " + maxAttempts + " seconds");
    }

    /**
     * Extracts port number from URL.
     */
    private int extractPort(String url) {
        try {
            // Remove protocol (http:// or https://)
            String withoutProtocol = url.replaceFirst("^https?://", "");
            // Extract port (e.g., localhost:8089 -> 8089)
            if (withoutProtocol.contains(":")) {
                String portStr = withoutProtocol.substring(withoutProtocol.lastIndexOf(":") + 1);
                // Remove any path after port
                if (portStr.contains("/")) {
                    portStr = portStr.substring(0, portStr.indexOf("/"));
                }
                return Integer.parseInt(portStr);
            }
            // Default ports
            return url.startsWith("https") ? 443 : 80;
        } catch (Exception e) {
            logger.warn("Could not extract port from URL: {}, using default 8089", url);
            return 8089;
        }
    }

    /**
     * Checks if running on Windows.
     */
    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    /**
     * Stops the rentey-service if it was started by this manager.
     */
    public void stopRenteyService() {
        if (renteyServiceProcess != null && renteyServiceProcess.isAlive()) {
            logger.info("Stopping rentey-service...");
            renteyServiceProcess.destroy();
            try {
                if (!renteyServiceProcess.waitFor(10, TimeUnit.SECONDS)) {
                    renteyServiceProcess.destroyForcibly();
                }
                logger.info("rentey-service stopped");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                renteyServiceProcess.destroyForcibly();
            }
        }
    }
}

