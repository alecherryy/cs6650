package clientTwo.model;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Locale;

/**
 * This class represents a Producer that implements
 * the Runnable interface. It's used to deliver data
 * to the data buffer.
 */
public class Consumer implements Runnable {
    private DataQueue buffer;
    private ArrayList<Record> records;

    /**
     * Class constructor.
     *
     * @param buffer of the consumer
     */
    public Consumer(ArrayList<Record> records, DataQueue buffer) {
        this.records = records;

        // check buffer
        if (buffer == null) {
            throw new IllegalArgumentException("Invalid argument.");
        }
        this.buffer = buffer;
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

                // start timer
                long start = System.nanoTime();
                // send data to server
                HttpResponse res = new Request("POST", "wordcount", data).send();
                // check response status code
                if (res.statusCode() == 200) {
                    // + 1 to success counter
                    buffer.success.getAndIncrement();
                } else {
                    // + 1 to fail counter
                    buffer.fail.getAndIncrement();
                    // if bad response, log err to system
                    System.err.println("Text Line:" + data + " - Error code: " + res);
                }
                // end timer
                long end = System.nanoTime();

                // create new record and add it to the array
                records.add(new Record(start, res.request().method().toUpperCase(Locale.ROOT),
                        Utils.calcLatency(start, end), res.statusCode()));
            }
        } catch (InterruptedException | JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}