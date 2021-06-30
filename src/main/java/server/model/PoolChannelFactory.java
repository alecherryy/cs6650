package server.model;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import org.apache.commons.pool2.BasePooledObjectFactory;
import com.rabbitmq.client.Channel;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.sql.SQLException;

/**
 * This class represents a Channel Factory.
 */
public class PoolChannelFactory extends BasePooledObjectFactory<Channel> {
    private final static DatabaseDao dao = new DatabaseDao();
    private final static String QUEUE_NAME = "RABBIT_QUEUE";
    private final static String EXCHANGE_NAME = "RABBIT_EXCHANGE";
    private Connection conn;

    /**
     * Class constructor.
     */
    public PoolChannelFactory() {
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

        // define callback
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), "UTF-8");

            // add record to the database
            try {
                dao.addRecord(msg.replace("{", "").replace("}", "").replaceAll(" ", ""));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        };
        // consume messages posted to the queue
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