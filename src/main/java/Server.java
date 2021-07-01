import server.controller.ServerController;
import server.model.PoolChannelFactory;
import com.rabbitmq.client.Channel;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import server.model.*;

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
    private PoolChannelWrapper pool;

    /**
     * Overload init() method to create a new channel pool.
     *
     * @throws ServletException
     */
    public void init() throws ServletException {
        super.init();
        GenericObjectPool<Channel> objPool = new GenericObjectPool<>(new PoolChannelFactory());
        GenericObjectPoolConfig<Channel> config = new GenericObjectPoolConfig<>();
        // prevent access if the pool is at capacity
//        config.setBlockWhenExhausted(true);
        // set min number of objects
        config.setMinIdle(5000);
        objPool.setConfig(config);
        this.pool = new PoolChannelWrapper(objPool);

        try {
            this.pool.getPool().preparePool();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        if (!hasValidParameters(req)) {
            res.getWriter().write("One or both parameters " +
                    "are missing");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String function = req.getPathInfo().split("/")[1];
        String word = req.getPathInfo().split("/")[2];

        if (word != null && function != null) {
            // create new database connection
            DatabaseDao dao = new DatabaseDao();
            int num = dao.readRecord(word);

            // return response
            res.getWriter().write("Total count for [" + word + "]: " + num);
            res.setStatus(HttpServletResponse.SC_OK);
        } else {
            res.getWriter().write("One or both parameters " +
                    "are missing");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
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
        try {
            ServerController controller = new ServerController(this.pool);
            int size = controller.start(function, str).size();

            // return response to client
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write("Total unique words: " + size);
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();
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