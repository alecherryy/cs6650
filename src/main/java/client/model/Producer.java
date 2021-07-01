package client.model;

import java.io.*;

/**
 * This class represents a Producer that implements
 * the Runnable interface. It's used to deliver data
 * to the data buffer.
 */
public class Producer implements Runnable {
    private FileReader file;
    private DataQueue buffer;

    /**
     * Class constructor.
     *
     * @param file containing the data for the producer
     * @param buffer of the producer
     * @throws IllegalArgumentException
     */
    public Producer(String file, DataQueue buffer) throws IllegalArgumentException, FileNotFoundException {
        // check arguments
        if (buffer == null || file == null) {
            throw new IllegalArgumentException("Invalid arguments.");
        }

        try {
            this.file = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found.");
        }
        this.buffer = buffer;
    }

    /**
     * Override run() method.
     */
    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(this.file)) {
            while (br.ready()) {
                // ignore empty lines
                String line = br.readLine().strip();
                if (!line.equals("\n")) {
                    buffer.buffer.put(br.readLine().replaceAll("[^a-zA-Z ]", "").strip());
                }
            }
            buffer.done = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
