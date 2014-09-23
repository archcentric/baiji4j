package com.ctriposs.baiji.rpc.samples.movie.filter;

import com.ctriposs.baiji.rpc.client.filter.HttpResponseFilter;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

/**
 * Created by yqdong on 2014/9/22.
 */
public class TestClientResponseFilter implements HttpResponseFilter {

    private final String _name;

    public TestClientResponseFilter(){
        this("Default");
    }

    public TestClientResponseFilter(String name) {
        _name = name;
    }

    @Override
    public void apply(HttpResponse response) {
        System.out.println(getClass().getName() + " - " + _name);

        for (Header header : response.getAllHeaders()) {
            System.out.println(String.format("\t%s: %s", header.getName(), header.getValue()));
        }
    }
}
