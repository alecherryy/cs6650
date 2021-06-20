import com.rabbitmq.client.ConnectionFactory;
import server.controller.ServerController;
import server.model.Task;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

/**
 * This class a Servlet app; it can be used to implement
 * HTTP request on the server.
 */
@WebServlet(urlPatterns = {"/textbody/*"})
public class Server extends HttpServlet {
    private final Task task = new Task();

    /**
     * This method overrides the HttpServlet doGet(). It returns void.
     *
     * @param req the Http request
     * @param res the server response
     * @throws ServletException if the servlet run into errors
     * @throws IOException      if the I/O operation fails or is interrupted
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // do something, some day
    }

    /**
     * This method overrides the HttpServlet doPost(). It returns void.
     *
     * @param req the Http request
     * @param res the server response
     * @throws ServletException if the servlet run into errors
     * @throws IOException      if the I/O operation fails or is interrupted
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
        String str = req.getParameter("message");

//        factory.setHost("52.206.15.156");

        try {
            ServerController controller = new ServerController(function, str);
            controller.start();
            res.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

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
}