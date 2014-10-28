package com.ctriposs.baiji.rpc.samples.movie;

import com.ctriposs.baiji.rpc.client.*;
import com.ctriposs.baiji.rpc.common.types.*;
import java.io.IOException;

public class MovieServiceClient extends ServiceClientBase<MovieServiceClient> {
    public static final String ORIGINAL_SERVICE_NAME = "Movie";

    public static final String ORIGINAL_SERVICE_NAMESPACE = "http://soa.ctriposs.com/baijirpc/sample/movie";

    public static final String CODE_GENERATOR_VERSION = "1.1.0.0";

    private MovieServiceClient(String baseUri) {
        super(MovieServiceClient.class, baseUri);
    }

    private MovieServiceClient(String serviceName, String serviceNamespace, String subEnv) throws ServiceLookupException {
        super(MovieServiceClient.class, serviceName, serviceNamespace, subEnv);
    }

    public static MovieServiceClient getInstance() {
        return ServiceClientBase.getInstance(MovieServiceClient.class);
    }

    public static MovieServiceClient getInstance(String baseUrl) {
        return ServiceClientBase.getInstance(MovieServiceClient.class, baseUrl);
    }

    public AddMovieResponseType addMovie(AddMovieRequestType request)
                                    throws Exception {
        return super.invoke("addMovie", request, AddMovieResponseType.class);
    }
    public UpdateMovieResponseType updateMovie(UpdateMovieRequestType request)
                                    throws Exception {
        return super.invoke("updateMovie", request, UpdateMovieResponseType.class);
    }
    public GetMovieByIdResponseType getMovieById(GetMovieByIdRequestType request)
                                    throws Exception {
        return super.invoke("getMovieById", request, GetMovieByIdResponseType.class);
    }
    public DeleteMovieByIdResponseType deleteMovieById(DeleteMovieByIdRequestType request)
                                    throws Exception {
        return super.invoke("deleteMovieById", request, DeleteMovieByIdResponseType.class);
    }
    public FindMoviesByGenreResponseType findMoviesByGenre(FindMoviesByGenreRequestType request)
                                    throws Exception {
        return super.invoke("findMoviesByGenre", request, FindMoviesByGenreResponseType.class);
    }
    public ResetMovieResponseType resetMovie(ResetMovieRequestType request)
                                    throws Exception {
        return super.invoke("resetMovie", request, ResetMovieResponseType.class);
    }
    public GetMoviesResponseType getMovies(GetMoviesRequestType request)
                                    throws Exception {
        return super.invoke("getMovies", request, GetMoviesResponseType.class);
    }
    public com.ctriposs.baiji.rpc.common.types.CheckHealthResponseType checkHealth(com.ctriposs.baiji.rpc.common.types.CheckHealthRequestType request)
                                    throws Exception {
        return super.invoke("checkHealth", request, com.ctriposs.baiji.rpc.common.types.CheckHealthResponseType.class);
    }
}