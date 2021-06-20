package server.model;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public class Sender {
    private Connection conn;
    private final static String QUEUE_NAME = "SERVER_QUEUE";

    /**
     * Class constructor.
     *
     * @param conn connection
     */
    public Sender(Connection conn) {
        this.conn = conn;
    }


    public void start(String str) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try {
            Channel channel = this.conn.createChannel();
            channel.exchangeDeclare(QUEUE_NAME, "fanout");

            channel.basicPublish(QUEUE_NAME, "", null, str.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
