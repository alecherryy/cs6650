package clientTwo.controller;

import clientTwo.model.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * This class represents a clientOne.controller for the client.
 */
public class ClientController {
    private String file;
    private int threads;
    protected DataQueue buffer = new DataQueue();
    protected ArrayList<Record> records = new ArrayList<>();

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
            consumer.submit(new Consumer(this.records, this.buffer));
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
        float time = Utils.calcLatency(start, end);
        float performance = buffer.getSuccess().floatValue() / time;

        // print summary
        System.out.println("SUMMARY");
        System.out.println("Successful requests: " + buffer.getSuccess());
        System.out.println("Unsuccessful requests: " + buffer.getFail());
        System.out.println("Run time: " + time + " s");
        System.out.println("Performance: " + performance + " req/s");

        // print records to console
        logRecords();
    }

    private void logRecords() {
        System.out.println("--------------------------------\n");
        ArrayList<Float> list = (ArrayList<Float>) this.records.stream().map(r -> r.getLatency()).collect(Collectors.toList());
        System.out.println("Mean POST response time: " + Utils.calcMean(list));
        System.out.println("Median POST response time: " + Utils.calcMedian(list));
        System.out.println("Max POST response time: " + Collections.max(list));
    }
}
