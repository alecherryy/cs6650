package clientOne.model;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

/**
 * This class represents a Producer that implements
 * the Runnable interface. It's used to deliver data
 * to the data buffer.
 */
public class Consumer implements Runnable {
    private DataQueue buffer;

    /**
     * Class constructor.
     *
     * @param buffer of the consumer
     */
    public Consumer(DataQueue buffer) {
        this.buffer = buffer;

        // check buffer
        if (buffer == null) {
            throw new IllegalArgumentException("Invalid argument.");
        }
    }

    /**
     * Override run() method.
     */
    @Override
    public void run() {
        // initialize incoming data stream
        String data;
        try {
            while (!buffer.done || !buffer.buffer.isEmpty()) {
                // get data from buffer
                data = buffer.buffer.take();

                // send data to server
                int res = new Request("POST", "wordcount", data).send();
                // check response status code
                if (res == 200) {
                    // + 1 to success counter
                    buffer.success.getAndIncrement();
                } else {
                    // + 1 to fail counter
                    buffer.fail.getAndIncrement();
                    // if bad response, log err to system
                    System.err.println("Text Line:" + data + " - Error code: " + res);
                }
            }
        } catch (InterruptedException | JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}