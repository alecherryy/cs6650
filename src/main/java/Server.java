import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * This class a Servlet app; it can be used to implement
 * HTTP request on the server.
 */
@WebServlet("/")
public class Server extends HttpServlet {
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

        if (hasValidParameters(req)) {
            res.getWriter().write("Request was successful.");
            res.setStatus(HttpServletResponse.SC_OK);
        } else {
            // if params are missing, return 400 error code
            res.getWriter().write("One or both parameters " +
                    "are missing");
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
        if (req.getRequestURI().split("/").length > 1) {
            return true;
        }
        return false;
    }
}
