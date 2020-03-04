package com.server;

import com.undertow.standalone.UndertowServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.locks.Condition;
import static com.util.cloud.DeploymentConfiguration.getProperty;

public class Server {
    private static final Logger log = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws ServletException, UnknownHostException {

        final String host = getProperty("undertow.host", "0.0.0.0");
        final Integer port = getProperty("undertow.port", 8080);

        final UndertowServer server = new UndertowServer(host, port, "undertow-httpexchange.war");

        final Condition newCondition = server.LOCK.newCondition();

        String f = InetAddress.getLocalHost().getHostAddress();
        System.out.println("this is it : -> " + f);
        log.debug("HHHH" + InetAddress.getLocalHost().getHostAddress());

        server.start();
        try {
            while (true)
                newCondition.awaitNanos(1);
        } catch (InterruptedException cause) {
            server.stop();
        }



    }

}
