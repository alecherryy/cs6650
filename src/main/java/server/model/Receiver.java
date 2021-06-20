package server.model;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

public class Receiver {
    private Connection conn;
    private final static String QUEUE_NAME = "SERVER_QUEUE";

    /**
     * Class constructor.
     *
     * @param conn connection
     */
    public Receiver(Connection conn) {
        this.conn = conn;
    }

    public void start() throws IOException {
        try {
            Channel channel = this.conn.createChannel();
            channel.exchangeDeclare(QUEUE_NAME, "fanout");
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, QUEUE_NAME, "");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                new String(delivery.getBody(), "UTF-8");
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
