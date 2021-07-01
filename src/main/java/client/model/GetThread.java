package client.model;

import java.io.IOException;

/**
 * This class represents a single thread which makes 10 GET calls
 * to the server.
 */
public class GetThread implements Runnable {
    private static final String[] arr = {"the", "available", "access", "else", "final", "if", "above", "disk", "database", "total"};
    public static float time;

    /**
     * Override run() mehtod.
     */
    @Override
    public void run() {
        for (String s : arr) {
            long start = System.nanoTime();
            try {
                Request req = new Request("GET", "wordcount", s);
                req.send();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            long end = System.nanoTime();
            float diff = ((float) (end - start) / 1000000000.f);
            time = (time + diff);
        }
    }

}
