package clientTwo.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class represents a DataBuffer in a
 * producer-consumer pattern.
 */
public class DataQueue {
    protected BlockingQueue<String> buffer;
    protected AtomicInteger success;
    protected AtomicInteger fail;
    protected boolean done = false;

    /**
     * Class constructor.
     */
    public DataQueue() {
        this.buffer = new LinkedBlockingDeque<>(1000);

        // set success to 0
        this.success = new AtomicInteger(0);

        // set success to 0
        this.fail = new AtomicInteger(0);
    }

    public BlockingQueue<String> getQueue() {
        return buffer;
    }

    public AtomicInteger getSuccess() {
        return success;
    }

    public AtomicInteger getFail() {
        return fail;
    }
}
