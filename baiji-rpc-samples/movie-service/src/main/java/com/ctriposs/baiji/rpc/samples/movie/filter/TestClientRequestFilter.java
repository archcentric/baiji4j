package com.ctriposs.baiji.rpc.samples.movie.filter;

import com.ctriposs.baiji.rpc.client.filter.HttpRequestFilter;
import org.apache.http.Header;
import org.apache.http.HttpRequest;

/**
 * Created by yqdong on 2014/9/22.
 */
public class TestClientRequestFilter implements HttpRequestFilter {

    private final String _name;

    public TestClientRequestFilter(){
        this("Default");
    }

    public TestClientRequestFilter(String name) {
        _name = name;
    }

    @Override
    public void apply(HttpRequest request) {
        System.out.println(getClass().getName() + " - " + _name);

        for (Header header : request.getAllHeaders()) {
            System.out.println(String.format("\t%s: %s", header.getName(), header.getValue()));
        }
    }
}
