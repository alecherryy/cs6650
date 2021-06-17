import model.Task;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.channels.Channel;
import java.util.HashMap;
import java.util.Locale;

/**
 * This class a Servlet app; it can be used to implement
 * HTTP request on the server.
 */
@WebServlet(urlPatterns = {"/textbody/*"})
public class Server extends HttpServlet {
    private final Task task = new Task();

    /**
     * This method overrides the HttpServlet doGet(). It returns void.
     * @param req the Http request
     * @param res the server response
     * @throws ServletException if the servlet run into errors
     * @throws IOException if the I/O operation fails or is interrupted
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // do something, some day
    }

    /**
     * This method overrides the HttpServlet doPost(). It returns void.
     * @param req the Http request
     * @param res the server response
     * @throws ServletException if the servlet run into errors
     * @throws IOException if the I/O operation fails or is interrupted
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/plain");

        // if URL is wrong, return error message
        if (!hasValidParameters(req)) {
            res.getWriter().write("One or both parameters " +
                    "are missing");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String function = req.getPathInfo().split("/")[1];
        String body = req.getReader().readLine();

        HashMap<String, Integer> data = evaluateMessage(function, body);

        if (data == null) {
            res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            res.getWriter().write("This operation cannot be performed.");
        } else {
            try {

            } catch (Exception e) {
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                res.getWriter().write("Something went wrong and the server failed..");
                e.printStackTrace();
            }

        }


        res.getWriter().write("Success!");

    }

    /**
     * Private helper method to check whether the request
     * parameters are valid.
     *
     * @param req of the POST request
     * @return true if all parameters are valid, otherwise returns false
     */
    private boolean hasValidParameters(HttpServletRequest req) {
        String url = req.getPathInfo();
        // check for valid URL
        if (url == null || url.isEmpty()) {
            return false;
        }

        return true;
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
                return this.task.countWords(str);
            case "CHARCOUNT":
                this.task.countChars(str);
                break;
            default:
                return null;
        }

        return null;
    }
}
