import server.model.PoolChannelFactory;
import com.rabbitmq.client.Channel;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import server.model.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;

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
        config.setBlockWhenExhausted(true);
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
        HashMap<String, Integer> msg = evaluateMessage(function, str);

        try {
            // return response to client
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write(msg.size());
            // borrow a channel from the pool
            Channel c = this.pool.getPool().borrowObject();
            // push message to the queue
            c.basicPublish("RABBIT_EXCHANGE", "", null, msg.toString().getBytes());
            // return channel to the pool
            this.pool.getPool().returnObject(c);
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