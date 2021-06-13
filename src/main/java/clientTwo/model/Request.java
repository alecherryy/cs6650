package clientTwo.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;

/**
 * This class represents an HTTP request. Each method can
 * be use to construct a new type of request.
 */
public class Request {
    private static String URL = "http://localhost:8080/app/textbody/";
    // define HTTP client
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofMillis(10000))
            .build();
    private HttpRequest req;
    private String type;
    private String function;

    /**
     * Class constructor.
     *
     * @param body of the HTTP request
     * @throws JsonProcessingException if the body format is invalid
     */
    public Request(String type, String function, String body) throws JsonProcessingException {
        if (type.isEmpty() || function.isEmpty()) {
            throw new IllegalArgumentException("One or more parameters are missing.");
        }
        this.type = type;
        this.function = function;
        // create new hash map
        var values = new HashMap<String, String>() {{
            put("message", body);
        }};

        // map key-value pairs
        var mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(values);

        // call private method to build POST request
        buildRequest(requestBody);
    }

    /**
     * Overload class constructor.
     *
     * @param body of the HTTP request
     * @throws JsonProcessingException if the body format is invalid
     */
    public Request(String type, String body) throws JsonProcessingException {
        if (type.isEmpty()) {
            throw new IllegalArgumentException("One or more parameters are missing.");
        }
        this.type = type;
        // create new hash map
        var values = new HashMap<String, String>() {{
            put("message", body);
        }};

        // map key-value pairs
        var mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(values);

        // call private method to build POST request
        buildRequest(requestBody);
    }

    /**
     * Send HTTP request to the server and return
     * response status code.
     *
     * @return the response status code, i.e. 200, 400 or 500
     */
    public HttpResponse send() throws IOException, InterruptedException {
        HttpResponse<String> res = HTTP_CLIENT.send(req, HttpResponse.BodyHandlers.ofString());

        return res;
    }

    /**
     * Private helper method to build a new request. Right now it
     * only returns a POST, but can be refactored to include requests
     * such as GET, PUT and delete.
     *
     * @param body of the request
     */
    private void buildRequest(String body) {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        switch (this.type) {
            case "GET":
                // do something some day
                break;
            case "POST":
                builder.POST(HttpRequest.BodyPublishers.ofString(body))
                        .uri(URI.create(URL + function));
                break;
            case "PUT":
                // do something some day
                break;
            case "DELETE":
                // do something some day
                break;
            default:
                // throw an error
                break;
        }
        this.req = builder.setHeader("User-Agent", "BSDS Application")
                .build();
    }
}
