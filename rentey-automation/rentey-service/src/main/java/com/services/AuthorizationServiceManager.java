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
 * Service to manage the authorization-service lifecycle.
 * Automatically starts authorization-service if it's not running when needed.
 */
@Service
public class AuthorizationServiceManager {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationServiceManager.class);
    
    private final String authorizationServiceBaseUrl;
    private final int authorizationServicePort;
    private Process authorizationServiceProcess;
    private final String projectRootPath;

    public AuthorizationServiceManager(
            @Value("${authorization.service.base-url}") String authorizationServiceBaseUrl) {
        this.authorizationServiceBaseUrl = authorizationServiceBaseUrl;
        this.authorizationServicePort = extractPort(authorizationServiceBaseUrl);
        this.projectRootPath = findProjectRoot();
    }

    /**
     * Finds the project root directory by looking for authorization-service directory.
     */
    private String findProjectRoot() {
        String currentDir = System.getProperty("user.dir");
        Path currentPath = Paths.get(currentDir);
        
        // Check if we're already at project root (authorization-service exists here)
        Path authorizationServicePath = currentPath.resolve("authorization-service");
        if (Files.exists(authorizationServicePath)) {
            logger.debug("Found project root at: {}", currentPath);
            return currentDir;
        }
        
        // Check if we're in rentey-service, go up one level
        if (currentPath.getFileName().toString().equals("rentey-service")) {
            Path parentPath = currentPath.getParent();
            if (Files.exists(parentPath.resolve("authorization-service"))) {
                logger.debug("Found project root at: {}", parentPath);
                return parentPath.toString();
            }
        }
        
        // Try going up from current directory
        Path searchPath = currentPath;
        for (int i = 0; i < 5; i++) { // Search up to 5 levels
            if (Files.exists(searchPath.resolve("authorization-service"))) {
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
     * Ensures authorization-service is running. If not, starts it automatically.
     */
    public void ensureAuthorizationServiceIsRunning() {
        if (isAuthorizationServiceRunning()) {
            logger.info("authorization-service is already running on port {}", authorizationServicePort);
            return;
        }

        logger.info("authorization-service is not running. Attempting to start it automatically...");
        startAuthorizationService();
        waitForServiceToBeReady();
    }

    /**
     * Checks if authorization-service is running by attempting to connect to its port.
     */
    private boolean isAuthorizationServiceRunning() {
        try (Socket socket = new Socket()) {
            socket.connect(new java.net.InetSocketAddress("localhost", authorizationServicePort), 1000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Starts authorization-service using Maven.
     */
    private void startAuthorizationService() {
        try {
            Path projectRoot = Paths.get(projectRootPath);
            Path authorizationServicePath = projectRoot.resolve("authorization-service");
            
            if (!Files.exists(authorizationServicePath)) {
                logger.error("authorization-service directory not found at: {}", authorizationServicePath);
                throw new RuntimeException("authorization-service directory not found at: " + authorizationServicePath);
            }

            // Determine Maven wrapper command based on OS
            String mavenCommand = isWindows() ? "mvnw.cmd" : "mvnw";
            Path mavenWrapper = authorizationServicePath.resolve(mavenCommand);
            
            // If mvnw doesn't exist in authorization-service, check project root
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
            
            processBuilder.directory(authorizationServicePath.toFile());
            processBuilder.redirectErrorStream(true);
            
            // Set environment variables if needed
            processBuilder.environment().put("MAVEN_OPTS", "-Xmx512m");
            
            // Start the process
            authorizationServiceProcess = processBuilder.start();
            
            logger.info("Started authorization-service process (PID: {}) from directory: {}", 
                    authorizationServiceProcess.pid(), authorizationServicePath);
            
            // Add shutdown hook to cleanup process when application stops
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (authorizationServiceProcess != null && authorizationServiceProcess.isAlive()) {
                    logger.info("Stopping authorization-service process...");
                    authorizationServiceProcess.destroy();
                    try {
                        if (!authorizationServiceProcess.waitFor(10, TimeUnit.SECONDS)) {
                            authorizationServiceProcess.destroyForcibly();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        authorizationServiceProcess.destroyForcibly();
                    }
                }
            }));
            
        } catch (Exception e) {
            logger.error("Failed to start authorization-service: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to start authorization-service automatically", e);
        }
    }

    /**
     * Waits for authorization-service to be ready by checking if the port is open.
     */
    private void waitForServiceToBeReady() {
        int maxAttempts = 60; // Wait up to 60 seconds
        int attempt = 0;
        
        logger.info("Waiting for authorization-service to be ready...");
        
        while (attempt < maxAttempts) {
            if (isAuthorizationServiceRunning()) {
                logger.info("authorization-service is now ready!");
                return;
            }
            
            try {
                Thread.sleep(1000); // Wait 1 second between attempts
                attempt++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for authorization-service", e);
            }
        }
        
        throw new RuntimeException("authorization-service did not become ready within " + maxAttempts + " seconds");
    }

    /**
     * Extracts port number from URL.
     */
    private int extractPort(String url) {
        try {
            // Remove protocol (http:// or https://)
            String withoutProtocol = url.replaceFirst("^https?://", "");
            // Extract port (e.g., localhost:8088 -> 8088)
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
            logger.warn("Could not extract port from URL: {}, using default 8088", url);
            return 8088;
        }
    }

    /**
     * Checks if running on Windows.
     */
    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    /**
     * Stops the authorization-service if it was started by this manager.
     */
    public void stopAuthorizationService() {
        if (authorizationServiceProcess != null && authorizationServiceProcess.isAlive()) {
            logger.info("Stopping authorization-service...");
            authorizationServiceProcess.destroy();
            try {
                if (!authorizationServiceProcess.waitFor(10, TimeUnit.SECONDS)) {
                    authorizationServiceProcess.destroyForcibly();
                }
                logger.info("authorization-service stopped");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                authorizationServiceProcess.destroyForcibly();
            }
        }
    }
}

