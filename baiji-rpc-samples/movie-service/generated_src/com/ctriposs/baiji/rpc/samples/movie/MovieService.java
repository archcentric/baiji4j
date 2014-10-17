package com.ctriposs.baiji.rpc.samples.movie;

import com.ctriposs.baiji.rpc.common.BaijiContract;

@BaijiContract(serviceName = "Movie", serviceNamespace = "http://soa.ctriposs.com/baijirpc/sample/movie", codeGeneratorVersion = "1.1.0.0")
public interface MovieService {

    AddMovieResponseType addMovie(AddMovieRequestType request) throws Exception;

    UpdateMovieResponseType updateMovie(UpdateMovieRequestType request) throws Exception;

    GetMovieByIdResponseType getMovieById(GetMovieByIdRequestType request) throws Exception;

    DeleteMovieByIdResponseType deleteMovieById(DeleteMovieByIdRequestType request) throws Exception;

    FindMoviesByGenreResponseType findMoviesByGenre(FindMoviesByGenreRequestType request) throws Exception;

    ResetMovieResponseType resetMovie(ResetMovieRequestType request) throws Exception;

    GetMoviesResponseType getMovies(GetMoviesRequestType request) throws Exception;

    com.ctriposs.baiji.rpc.common.types.CheckHealthResponseType checkHealth(com.ctriposs.baiji.rpc.common.types.CheckHealthRequestType request) throws Exception;
}