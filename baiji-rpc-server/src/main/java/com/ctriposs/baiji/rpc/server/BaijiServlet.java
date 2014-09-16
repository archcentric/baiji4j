package com.ctriposs.baiji.rpc.server;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * An {@link javax.servlet.http.HttpServlet} that responds to Baiji RPC requests.
 */
public class BaijiServlet extends BaijiServletBase {

    @Override
    public void service(ServletRequest req, ServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }
}
