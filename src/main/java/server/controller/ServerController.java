package server.controller;

import com.rabbitmq.client.Channel;
import server.model.PoolChannelWrapper;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import server.model.Task;

import java.util.HashMap;

/**
 * This class represents a Controller for the client.
 */
public class ServerController {
    private PoolChannelWrapper pool;

    /**
     * Class constructor.
     *
     * @param pool of channels
     */
    public ServerController(PoolChannelWrapper pool) {
        this.pool = pool;
    }

    /**
     * Process messages and add them to RabbitMQ.
     *
     * @param function
     * @param str
     * @return
     */
    public HashMap<String, Integer> start(String function, String str) {
        HashMap<String, Integer> msg = evaluateMessage(function, str);

        Channel c = null;
        try {
            // borrow a channel from the pool
            c = this.pool.getPool().borrowObject();
            // push message to the queue
            c.basicPublish("RABBIT_EXCHANGE", "", null, msg.toString().getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
        // return channel to the pool
        this.pool.getPool().returnObject(c);

        return msg;
    }

    /**
     * Private helper method to check whether the function
     * exists.
     *
     * @param function of the POST request
     * @return true if all parameters are valid, otherwise returns false
     */
    private HashMap<String, Integer> evaluateMessage(String function, String str) {
        String val = function.toUpperCase();

        switch (val) {
            case "WORDCOUNT":
                return new Task().countWords(str);
            case "CHARCOUNT":
                new Task().countChars(str);
                break;
            default:
                return null;
        }

        return null;
    }
}