package server.model;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * This class represents a Channel Factory.
 */
public class PoolReceiverFactory extends BasePooledObjectFactory<Channel> {
    private final static String QUEUE_NAME = "RABBIT_QUEUE";
    private final static String EXCHANGE_NAME = "RABBIT_EXCHANGE";
    private Connection conn;

    /**
     * Class constructor.
     */
    public PoolReceiverFactory() {
        super();
        // open a new connection with RabbitMQ
        conn = new PoolConnection().getConnection();
    }

    /**
     * Override default method to create a new channel.
     *
     * @return a new Channel
     * @throws Exception
     */
    @Override
    public Channel create() throws Exception {
        Channel channel = conn.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME,"");


        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), "UTF-8");
            System.out.println("Current message: " + msg);
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });

        return channel;
    }

    /**
     * Override default method to return a pool object.
     *
     * @param channel to add to the pool
     * @return the pool
     */
    @Override
    public PooledObject<Channel> wrap(Channel channel) {
        return new DefaultPooledObject<Channel>(channel);
    }
}