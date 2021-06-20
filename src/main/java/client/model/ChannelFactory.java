package client.model;

import com.rabbitmq.client.Connection;
import org.apache.commons.pool2.BasePooledObjectFactory;
import com.rabbitmq.client.Channel;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import server.model.RabbitConnection;

/**
 * This class represents a Channel Factory.
 */
public class ChannelFactory extends BasePooledObjectFactory<Channel> {
    private final static String QUEUE_NAME = "RABBIT_QUEUE";
    private final static String EXCHANGE_NAME = "RABBIT_EXCHANGE";
    private Connection config;

    /**
     * Class constructor.
     */
    public ChannelFactory() {
        super();
        // open a new connection with RabbitMQ
        config = new RabbitConnection().getConnection();
    }

    /**
     * Override default method to create a new channel.
     *
     * @return a new Channel
     * @throws Exception
     */
    @Override
    public Channel create() throws Exception {
        Channel channel = config.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME,"");

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