package server.model;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Receiver {
    private final static String QUEUE_NAME = "SERVER_QUEUE";


    public Receiver() {}

    public void start() throws IOException {
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("localhost");
//
//        try {
//            Connection conn = factory.newConnection();
//            Channel channel = conn.createChannel();
//            channel.exchangeDeclare(QUEUE_NAME, "fanout");
//            String queueName = channel.queueDeclare().getQueue();
//            channel.queueBind(queueName, QUEUE_NAME, "");
//
//            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
//
//            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//                String message = new String(delivery.getBody(), "UTF-8");
//                System.out.println(" [x] Received '" + message + "'");
//            };
//            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
//        } catch (IOException | TimeoutException e) {
//            e.printStackTrace();
//        }
    }
}
