package clientOne.controller;

import clientOne.model.Consumer;
import clientOne.model.DataQueue;
import clientOne.model.Producer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * This class represents a clientOne.controller for the client.
 */
public class ClientController {
    private String file;
    private int threads;
    protected DataQueue buffer = new DataQueue();

    /**
     * Class constructor.
     *
     * @param file to feed the producer
     * @param threads to run
     */
    public ClientController(String file, int threads) {
        this.file = file;
        this.threads = threads;
    }

    /**
     * Method to start the producer-consumer pattern.
     */
    public void start() {
        // replace fix queue size with user input, at some point
//        DataQueue buffer = new DataQueue(file.size());

        // initialize the producer and consumer executorService
        ExecutorService producer = Executors.newFixedThreadPool(threads);
        ExecutorService consumer = Executors.newFixedThreadPool(threads);

        // Start timer
        long start = System.nanoTime();

        // run producer executor pool.
        try {
            producer.submit(new Producer(file, this.buffer));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found.");
        }

        // spun consumer threads
        for (int i = 0; i < threads; i++) {
            consumer.submit(new Consumer(this.buffer));
        }

        // terminate all threads
        producer.shutdown();
        consumer.shutdown();

        try {
            if (!producer.awaitTermination(10, TimeUnit.MINUTES)) {
                producer.shutdownNow();
            }
            if (!consumer.awaitTermination(10, TimeUnit.MINUTES)) {
                consumer.shutdownNow();
            }
        } catch (InterruptedException e) {
            // stop everything if exception
            producer.shutdownNow();
            consumer.shutdownNow();
        }

        // end timer
        long end = System.nanoTime();

        // indication for all thread termination
        System.out.println("Done.");
        System.out.println("--------------------------------\n");

        // calculate total time of running
        float time = ((float) (end - start) / 1000000000.f);
        float performance = buffer.getSuccess().floatValue() / time;

        // print summary
        System.out.println("SUMMARY");
        System.out.println("Successful requests: " + buffer.getSuccess());
        System.out.println("Unsuccessful requests: " + buffer.getFail());
        System.out.println("Run time: " + time + " s");
        System.out.println("Performance: " + performance + " req/s");
    }

}
