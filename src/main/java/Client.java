import client.controller.ClientController;
import client.model.GetThread;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class represents a client.
 */
public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("-- ENTER INPUT <file> and <threads> --");
        Scanner in = new Scanner(System.in);

        String file = in.nextLine();
        System.out.println("Path to file: " + file);

        int threads = in.nextInt();
        System.out.println("Max number of threads: " + threads);

       in.close();

        ClientController controller = new ClientController(file, threads);
        // instantiate clientOne.controller
        controller.start();

        // star GetThread
        GetThread thread = new GetThread();
        Thread t = new Thread(thread);

        // put thread to sleep for 1 second
        Thread.currentThread().sleep(1000);
        t.start();
        t.join();

        System.out.println("\n");
        System.out.println("----- GET PERFORMANCE -----");
        System.out.println("Mean response time per GET request: " + GetThread.time / 10 + "/s");

    }
}