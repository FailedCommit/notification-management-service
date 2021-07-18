package com.manning.notification.manager.util;

import lombok.AllArgsConstructor;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

@AllArgsConstructor
public class H2ServerStarter {
    private static final Logger logger = LoggerFactory.getLogger(H2ServerStarter.class);

    public static void startH2Server() {
        try {
            Server h2Server = Server.createTcpServer(
                    "-tcp", "-tcpAllowOthers", "-tcpPort", "9091", "-ifNotExists").start();
            if (h2Server.isRunning(true)) {
                logger.info("H2 server was started and is running.");
            } else {
                throw new RuntimeException("Could not start H2 server.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to start H2 server: ", e);
        }
    }
}
