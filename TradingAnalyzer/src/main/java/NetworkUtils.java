import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkUtils {

    /**
     * Gets the first non-loopback IPv4 address of the machine
     * @return IP address as String or null if not found
     */
    public static String getMachineIpAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // Skip loopback and inactive interfaces
                if (iface.isLoopback() || !iface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    // Return the first non-loopback IPv4 address
                    if (!addr.isLoopbackAddress() && addr.getHostAddress().indexOf(':') == -1) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            System.err.println("Error getting network interfaces: " + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        String ipAddress = getMachineIpAddress();
        if (ipAddress != null) {
            System.out.println("Current machine IP: " + ipAddress);
        } else {
            System.out.println("Could not determine IP address");
        }
    }
}