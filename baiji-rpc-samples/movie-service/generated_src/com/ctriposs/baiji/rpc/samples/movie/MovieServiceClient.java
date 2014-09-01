package com.ctriposs.baiji.rpc.samples.movie;

import com.ctriposs.baiji.rpc.client.*;
import com.ctriposs.baiji.rpc.common.types.*;
import java.io.IOException;

public class MovieServiceClient extends ServiceClientBase<MovieServiceClient> {
    public static final String ORIGINAL_SERVICE_NAME = "Movie";

    public static final String ORIGINAL_SERVICE_NAMESPACE = "http://soa.ctriposs.com/baijirpc/sample/movie";

    private MovieServiceClient(String baseUri) {
        super(MovieServiceClient.class, baseUri);
    }

    private MovieServiceClient(String serviceName, String serviceNamespace, String subEnv) throws ServiceLookupException {
        super(MovieServiceClient.class, serviceName, serviceNamespace, subEnv);
    }

    public AddMovieResponseType addMovie(AddMovieRequestType request)
                                    throws ServiceException, HttpWebException, IOException {
        return super.invoke("addMovie", request, AddMovieResponseType.class);
    }
    public UpdateMovieResponseType updateMovie(UpdateMovieRequestType request)
                                    throws ServiceException, HttpWebException, IOException {
        return super.invoke("updateMovie", request, UpdateMovieResponseType.class);
    }
    public GetMovieByIdResponseType getMovieById(GetMovieByIdRequestType request)
                                    throws ServiceException, HttpWebException, IOException {
        return super.invoke("getMovieById", request, GetMovieByIdResponseType.class);
    }
    public DeleteMovieByIdResponseType deleteMovieById(DeleteMovieByIdRequestType request)
                                    throws ServiceException, HttpWebException, IOException {
        return super.invoke("deleteMovieById", request, DeleteMovieByIdResponseType.class);
    }
    public FindMoviesByGenreResponseType findMoviesByGenre(FindMoviesByGenreRequestType request)
                                    throws ServiceException, HttpWebException, IOException {
        return super.invoke("findMoviesByGenre", request, FindMoviesByGenreResponseType.class);
    }
    public ResetMovieResponseType resetMovie(ResetMovieRequestType request)
                                    throws ServiceException, HttpWebException, IOException {
        return super.invoke("resetMovie", request, ResetMovieResponseType.class);
    }
    public GetMoviesResponseType getMovies(GetMoviesRequestType request)
                                    throws ServiceException, HttpWebException, IOException {
        return super.invoke("getMovies", request, GetMoviesResponseType.class);
    }
    public com.ctriposs.baiji.rpc.common.types.CheckHealthResponseType checkHealth(com.ctriposs.baiji.rpc.common.types.CheckHealthRequestType request)
                                    throws ServiceException, HttpWebException, IOException {
        return super.invoke("checkHealth", request, com.ctriposs.baiji.rpc.common.types.CheckHealthResponseType.class);
    }
}