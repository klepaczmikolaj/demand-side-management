package pl.wut.wsd.dsm.agent.infrastructure;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Optional;

@Slf4j
public class InetUtils {

    public static Optional<String> getMyHostname() {
        try {
            final InetAddress ip = InetAddress.getLocalHost();
            return Optional.of(ip.getHostName());
        } catch (final UnknownHostException e) {
            log.error("Cannot find local hostname", e);
            return Optional.empty();
        }
    }

    public static int getFreePort() {
        try (final ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
