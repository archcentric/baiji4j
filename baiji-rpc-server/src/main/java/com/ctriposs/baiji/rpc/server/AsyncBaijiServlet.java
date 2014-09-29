package com.ctriposs.baiji.rpc.server;

import com.ctriposs.baiji.rpc.common.logging.Logger;
import com.ctriposs.baiji.rpc.common.logging.LoggerFactory;
import com.ctriposs.baiji.rpc.common.util.DaemonThreadFactory;
import com.ctriposs.baiji.rpc.server.util.ConfigUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * An async {@link javax.servlet.http.HttpServlet} that responds to Baiji RPC requests.
 */
public class AsyncBaijiServlet extends BaijiServletBase {

    private static final int DEFAULT_ASYNC_TIMEOUT = 60 * 1000; // In ms.
    private static final int DEFAULT_CORE_THREAD_COUNT = 20;
    private static final int DEFAULT_MAX_THREAD_COUNT = 100;
    private static final int DEFAULT_KEEP_ALIVE_TIME = 60 * 1000;

    // Servlet Init Params
    private static final String ASYNC_TIMEOUT_PARAM = "async-timeout";
    private static final String CORE_THREAD_COUNT_PARAM = "core-thread-count";
    private static final String MAX_THREAD_COUNT_PARAM = "max-thread-count";
    private static final String KEEP_ALIVE_TIME_PARAM = "keepalive-time";

    private ThreadPoolExecutor _poolExecutor;
    private int _asyncTimeout;
    private int _coreThreadCount;
    private int _maxThreadCount;
    private int _keepAliveTime;

    @Override
    public void init() {
        super.init();

        ServletConfig servletConfig = getServletConfig();
        _asyncTimeout = ConfigUtil.getIntConfig(servletConfig, ASYNC_TIMEOUT_PARAM, DEFAULT_ASYNC_TIMEOUT);
        _coreThreadCount = ConfigUtil.getIntConfig(servletConfig, CORE_THREAD_COUNT_PARAM, DEFAULT_CORE_THREAD_COUNT);
        _maxThreadCount = ConfigUtil.getIntConfig(servletConfig, MAX_THREAD_COUNT_PARAM, DEFAULT_MAX_THREAD_COUNT);
        _keepAliveTime = ConfigUtil.getIntConfig(servletConfig, KEEP_ALIVE_TIME_PARAM, DEFAULT_KEEP_ALIVE_TIME);

        _poolExecutor = new ThreadPoolExecutor(_coreThreadCount, _maxThreadCount, _keepAliveTime, TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(), new DaemonThreadFactory());
    }

    @Override
    public void service(ServletRequest req, ServletResponse resp)
            throws ServletException, IOException {
        if (_poolExecutor == null) {
            throw new IllegalStateException("The servlet has been destroyed.");
        }

        req.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);

        AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(_asyncTimeout);
        AsyncBaijiListener listener = new AsyncBaijiListener();
        asyncContext.addListener(listener);
        try {
            _poolExecutor.submit(new BaijiCallable(asyncContext, listener));
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @Override
    public void destroy() {
        if (_poolExecutor != null) {
            try {
                _poolExecutor.awaitTermination(_asyncTimeout, TimeUnit.MILLISECONDS);
                _poolExecutor.shutdown();
            } catch (InterruptedException e) {
                _poolExecutor.shutdownNow();
                _poolExecutor = null;
            }
        }
        super.destroy();
    }

    private class BaijiCallable implements Callable {
        private final AsyncContext _context;
        private final AsyncBaijiListener _listener;

        public BaijiCallable(AsyncContext context, AsyncBaijiListener listener) {
            _context = context;
            _listener = listener;
        }

        @Override
        public Object call() throws Exception {
            try {
                processRequest(_context.getRequest(), _context.getResponse());
            } catch (Throwable t) {
                _logger.error("BaijiCallable execute error.", t);
            } finally {
                if (!_listener.isTimedOut()) {
                    try {
                        _context.complete();
                    } catch (Throwable t) {
                        _logger.error("AsyncContext complete error.", t);
                    }
                }
            }
            return null;
        }
    }

    private static class AsyncBaijiListener implements AsyncListener {
        private static final Logger _logger = LoggerFactory.getLogger(AsyncBaijiListener.class);

        private boolean _timedOut;

        public boolean isTimedOut() {
            return _timedOut;
        }

        @Override
        public void onComplete(AsyncEvent event) throws IOException {
        }

        @Override
        public void onTimeout(AsyncEvent event) throws IOException {
            _timedOut = true;
            _logger.error(String.format("Access %s timeout in AsyncBaijiServlet.",
                    ((HttpServletRequest) event.getAsyncContext().getRequest()).getRequestURL()));
        }

        @Override
        public void onError(AsyncEvent event) throws IOException {
            _logger.error(String.format("Error occurs while access %s in AsyncBaijiServlet.",
                    ((HttpServletRequest) event.getAsyncContext().getRequest()).getRequestURL()));
        }

        @Override
        public void onStartAsync(AsyncEvent event) throws IOException {
        }
    }
}
