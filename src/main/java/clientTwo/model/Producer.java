package clientTwo.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
                if (!br.readLine().isEmpty()) {
                    buffer.buffer.put(br.readLine());
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
