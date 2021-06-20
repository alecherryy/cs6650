package server.model;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * This class represents a connection for the Sender/Receiver
 * messaging pattern.
 */
public class PoolConnection {
    private Connection conn;

    /**
     * Class constructor.
     */
    public PoolConnection() {
        Connection newConn = createConnection();

        try {
            this.conn = newConn;
        } catch (NullPointerException e) {
            System.out.println("Could not establish a connection");
        }
    }

    /**
     * Getter method to return the connection field.
     *
     * @return the connection
     */
    public Connection getConnection() {
        return this.conn;
    }

    /**
     * Private helper method to create and establish a new connection.
     *
     * @return a new connection object
     */
    private Connection createConnection() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
//        factory.setHost("52.206.15.156");

        try {
            return factory.newConnection();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

        return null;
    }
}
