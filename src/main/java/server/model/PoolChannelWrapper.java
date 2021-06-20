package server.model;

import com.rabbitmq.client.Channel;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * This class represents a wrapper for a GenericObjectPool
 * class. It's needed to create a new pool in the Server class and avoid
 * an exception.
 */
public class PoolChannelWrapper {
    private final GenericObjectPool<Channel> pool;

    /**
     * Class constructor.
     *
     * @param pool
     */
    public PoolChannelWrapper(GenericObjectPool<Channel> pool) {
        this.pool = pool;
    }

    /**
     * Getter method to return the pool field.
     *
     * @return the pool
     */
    public GenericObjectPool<Channel> getPool() {
        return pool;
    }
}

