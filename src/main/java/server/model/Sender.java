package server.model;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Sender {
    private final static String QUEUE_NAME = "SERVER_QUEUE";


    public Sender() {}

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        String str = "A message";

        try {
            Connection conn = factory.newConnection();
            Channel channel = conn.createChannel();
            channel.exchangeDeclare(QUEUE_NAME, "fanout");

            channel.basicPublish(QUEUE_NAME, "", null, str.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + str + "'");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void start(String str) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try {
            Connection conn = factory.newConnection();
            Channel channel = conn.createChannel();
            channel.exchangeDeclare(QUEUE_NAME, "fanout");

            channel.basicPublish(QUEUE_NAME, "", null, str.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + str + "'");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
