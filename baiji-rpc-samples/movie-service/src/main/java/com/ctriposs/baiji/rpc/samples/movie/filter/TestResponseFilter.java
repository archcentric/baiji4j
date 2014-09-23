package com.ctriposs.baiji.rpc.samples.movie.filter;

import com.ctriposs.baiji.rpc.server.HttpRequestWrapper;
import com.ctriposs.baiji.rpc.server.HttpResponseWrapper;
import com.ctriposs.baiji.rpc.server.ServiceHost;
import com.ctriposs.baiji.rpc.server.filter.ResponseFilter;

/**
 * Created by yqdong on 2014/9/22.
 */
public class TestResponseFilter implements ResponseFilter {

    private final String _name;

    public TestResponseFilter(){
        this("Default");
    }

    public TestResponseFilter(String name) {
        _name = name;
    }

    @Override
    public void apply(ServiceHost host, HttpRequestWrapper request, HttpResponseWrapper response, Object responseObj) {
        System.out.println(String.format("Executing %s Name = %s Response = %s", getClass().getSimpleName(), _name,
                responseObj.getClass().getSimpleName()));    }
}
