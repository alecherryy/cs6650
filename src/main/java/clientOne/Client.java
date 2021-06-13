package clientOne;

import clientOne.controller.ClientController;

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
    }
}