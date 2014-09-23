package com.ctriposs.baiji.rpc.samples.movie.filter;

import com.ctriposs.baiji.rpc.server.HttpRequestWrapper;
import com.ctriposs.baiji.rpc.server.HttpResponseWrapper;
import com.ctriposs.baiji.rpc.server.ServiceHost;
import com.ctriposs.baiji.rpc.server.filter.PreRequestFilter;

/**
 * Created by yqdong on 2014/9/22.
 */
public class TestPreRequestFilter implements PreRequestFilter {

    private final String _name;

    public TestPreRequestFilter(){
        this("Default");
    }

    public TestPreRequestFilter(String name) {
        _name = name;
    }
    @Override
    public void apply(ServiceHost host, HttpRequestWrapper request, HttpResponseWrapper response) {
        System.out.println(String.format("Executing %s Name = %s", getClass().getSimpleName(), _name));
    }
}
