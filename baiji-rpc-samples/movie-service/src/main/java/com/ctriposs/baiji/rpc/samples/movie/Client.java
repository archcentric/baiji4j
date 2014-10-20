package com.ctriposs.baiji.rpc.samples.movie;

import com.ctriposs.baiji.rpc.client.ServiceClientConfig;
import com.ctriposs.baiji.rpc.samples.movie.filter.TestClientRequestFilter;
import com.ctriposs.baiji.rpc.samples.movie.filter.TestClientResponseFilter;

/**
 * Created by yqdong on 2014/9/8.
 */
public class Client {

    public static void main(String[] args) throws Exception {
//        ServiceClientConfig config = new ServiceClientConfig();
//        config.setServiceRegistryUrl("http://localhost:4001");
//        config.setSubEnv("dev");
//        config.setServiceSubEnv(MovieServiceClient.ORIGINAL_SERVICE_NAME, MovieServiceClient.ORIGINAL_SERVICE_NAMESPACE, "dev");
//        MovieServiceClient.initialize(config);

        MovieServiceClient.setGlobalHttpRequestFilter(new TestClientRequestFilter("Global"));
        MovieServiceClient.setGlobalHttpResponseFilter(new TestClientResponseFilter("Global"));

//        MovieServiceClient client = MovieServiceClient.getInstance();
        MovieServiceClient client = MovieServiceClient.getInstance("http://localhost:8112/");
        client.headers().put("Test-Header", "Test-Value");
        client.setLocalHttpRequestFilter(new TestClientRequestFilter("Local"));
        client.setLocalHttpResponseFilter(new TestClientResponseFilter("Local"));
        GetMoviesRequestType request = new GetMoviesRequestType(5);
        GetMoviesResponseType response = client.getMovies(request);
        if (response.movies == null || response.movies.isEmpty()) {
            System.out.println("No movie found.");
        } else {
            for (MovieDto movie : response.movies) {
                System.out.println(String.format("#%s %s", movie.id, movie.title));
            }
        }

        System.exit(0);
    }
}
