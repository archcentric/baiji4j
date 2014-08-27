package com.ctriposs.baiji.rpc.samples.movie;

import com.ctriposs.baiji.rpc.common.BaijiContract;

@BaijiContract(serviceName = "Movie", serviceNamespace = "http://soa.ctriposs.com/baijirpc/sample/movie", codeGeneratorVersion = "1.0.0.0")
public interface MovieService {

    AddMovieResponseType addMovie(AddMovieRequestType request);

    UpdateMovieResponseType updateMovie(UpdateMovieRequestType request);

    GetMovieByIdResponseType getMovieById(GetMovieByIdRequestType request);

    DeleteMovieByIdResponseType deleteMovieById(DeleteMovieByIdRequestType request);

    FindMoviesByGenreResponseType findMoviesByGenre(FindMoviesByGenreRequestType request);

    ResetMovieResponseType resetMovie(ResetMovieRequestType request);

    GetMoviesResponseType getMovies(GetMoviesRequestType request);

    com.ctriposs.baiji.rpc.common.types.CheckHealthResponseType checkHealth(com.ctriposs.baiji.rpc.common.types.CheckHealthRequestType request);
}