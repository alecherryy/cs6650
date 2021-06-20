package server.controller;

import server.model.RabbitConnection;
import server.model.Task;

import java.io.IOException;
import java.util.HashMap;

/**
 * This class represents a controller for the server.
 */
public class ServerController {
    private String function;
    private String message;

    /**
     * Class constructor.
     *
     * @param message for the server
     */
    public ServerController(String function, String message) {
        this.function = function;
        this.message = message;
    }

    /**
     * Method to start the producer-consumer pattern.
     */
    public void start() throws IOException {
        HashMap<String, Integer> data = evaluateMessage(function, message);

        // create new RabbitMQ connection
        RabbitConnection conn = new RabbitConnection();
//        conn.getConnection();
    }

    /**
     * Private helper method to check whether the request
     * parameters are valid.
     *
     * @param function of the POST request
     * @return true if all parameters are valid, otherwise returns false
     */
    private HashMap<String, Integer> evaluateMessage(String function, String str) {
        String val = function.toUpperCase();

        switch (val) {
            case "WORDCOUNT":
                return new Task().countWords(str);
            case "CHARCOUNT":
                new Task().countChars(str);
                break;
            default:
                return null;
        }

        return null;
    }
}
