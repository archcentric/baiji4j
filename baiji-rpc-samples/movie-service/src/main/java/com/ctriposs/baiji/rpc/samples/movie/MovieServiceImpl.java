package com.ctriposs.baiji.rpc.samples.movie;

import com.ctriposs.baiji.rpc.common.types.CheckHealthRequestType;
import com.ctriposs.baiji.rpc.common.types.CheckHealthResponseType;
import com.ctriposs.baiji.rpc.samples.movie.filter.*;
import com.ctriposs.baiji.rpc.server.filter.*;
import org.apache.commons.lang.NullArgumentException;

import java.util.ArrayList;
import java.util.List;

public class MovieServiceImpl implements MovieService {

    private static final List<MovieDto> defaultMovies = new ArrayList<MovieDto>();

    private final List<MovieDto> movies = new ArrayList<MovieDto>(defaultMovies);

    private long lastInsertedId = defaultMovies.size();

    private final Object syncRoot = new Object();

    static {
        long id = 0;

        MovieDto movie = new MovieDto(++id, "tt1375666", "Inception", 9.2,
                "Christopher Nolan", "2010/07/16",
                "Your mind is the scene of the crime", new ArrayList<String>());
        movie.genres.add("Action");
        movie.genres.add("Thriller");
        movie.genres.add("Sci-Fi");
        defaultMovies.add(movie);

        movie = new MovieDto(++id, "tt0111161", "The Shawshank Redemption",
                9.2, "Frank Darabont", "1995/02/17",
                "Fear can hold you prisoner. Hope can set you free.",
                new ArrayList<String>());
        movie.genres.add("Crime");
        movie.genres.add("Drama");
        defaultMovies.add(movie);

        movie = new MovieDto(++id, "tt0071562", "The Godfather: Part II", 9.0,
                "Francis Ford Coppola", "1974/12/20",
                "An offer you can't refuse.", new ArrayList<String>());
        movie.genres.add("Crime");
        movie.genres.add("Drama");
        movie.genres.add("Thriller");
        defaultMovies.add(movie);

        movie = new MovieDto(++id, "tt0068646", "The Godfather", 9.2,
                "Francis Ford Coppola", "1972/03/24",
                "An offer you can't refuse.", new ArrayList<String>());
        movie.genres.add("Crime");
        movie.genres.add("Drama");
        movie.genres.add("Thriller");
        defaultMovies.add(movie);

        movie = new MovieDto(
                ++id,
                "tt0060196",
                "The Good, the Bad and the Ugly",
                9.0,
                "Sergio Leone",
                "1967/12/29",
                "They formed an alliance of hate to steal a fortune n dead man's gold",
                new ArrayList<String>());
        movie.genres.add("Adventure");
        movie.genres.add("Western");
        defaultMovies.add(movie);
    }

    @Override
    public AddMovieResponseType addMovie(AddMovieRequestType request) {
        if (request.movie == null) {
            throw new NullArgumentException("movie");
        }
        synchronized (syncRoot) {
            request.movie.id = ++lastInsertedId;
            movies.add(request.movie);
            return new AddMovieResponseType(null, lastInsertedId);
        }
    }

    @Override
    public UpdateMovieResponseType updateMovie(UpdateMovieRequestType request) {
        if (request.movie == null) {
            throw new NullArgumentException("movie");
        }
        Long movieId = request.movie.id;
        int targetIndex = -1;
        for (int i = 0; i < movies.size(); ++i) {
            if (movieId.equals(movies.get(i).id)) {
                targetIndex = i;
                break;
            }
        }
        if (targetIndex == -1) {
            throw new RuntimeException("The specified movie not found.");
        }
        synchronized (syncRoot) {
            if (movies.get(targetIndex).id != movieId) {
                throw new RuntimeException("The specified movie not found.");
            }
            movies.set(targetIndex, request.movie);
        }
        return new UpdateMovieResponseType(null);
    }

    @Override
    public GetMovieByIdResponseType getMovieById(GetMovieByIdRequestType request) {
        if (request.id <= 0) {
            throw new IllegalArgumentException("id must be greater than 0.");
        }
        MovieDto targetMovie = null;
        for (int i = 0; i < movies.size(); ++i) {
            MovieDto movie = movies.get(i);
            if (request.id.equals(movies.get(i).id)) {
                targetMovie = movie;
                break;
            }
        }
        return new GetMovieByIdResponseType(null, targetMovie);
    }

    @Override
    public DeleteMovieByIdResponseType deleteMovieById(
            DeleteMovieByIdRequestType request) {
        if (request.id <= 0) {
            throw new IllegalArgumentException("id must be greater than 0.");
        }
        int targetIndex = -1;
        for (int i = 0; i < movies.size(); ++i) {
            if (request.id.equals(movies.get(i).id)) {
                targetIndex = i;
                break;
            }
        }
        if (targetIndex == -1) {
            throw new RuntimeException("The specified movie not found.");
        }
        synchronized (syncRoot) {
            if (movies.get(targetIndex).id != request.id) {
                throw new RuntimeException("The specified movie not found.");
            }
            movies.remove(request.id);
        }
        return new DeleteMovieByIdResponseType(null);
    }

    @Override
    public FindMoviesByGenreResponseType findMoviesByGenre(
            FindMoviesByGenreRequestType request) {
        if (request.genre == null) {
            throw new NullArgumentException("genre");
        }
        String loweredGenre = request.genre.toLowerCase();
        List<MovieDto> retMovies = new ArrayList<MovieDto>();
        for (int i = 0; i < movies.size(); ++i) {
            MovieDto movie = movies.get(i);
            if (request.genre.toLowerCase().contains(loweredGenre)) {
                retMovies.add(movie);
                break;
            }
        }
        return new FindMoviesByGenreResponseType(null, retMovies);
    }

    @Override
    public ResetMovieResponseType resetMovie(ResetMovieRequestType request) {
        synchronized (syncRoot) {
            movies.clear();
            movies.addAll(defaultMovies);
        }
        return new ResetMovieResponseType(null);
    }

    @Override
    @WithRequestFilters({
            @WithRequestFilter(value = TestRequestFilter.class, priority = -1),
            @WithRequestFilter(TestRequestFilter.class)
    })
    @WithResponseFilters({
            @WithResponseFilter(value = TestResponseFilter.class, priority = -1),
            @WithResponseFilter(TestResponseFilter.class)
    })
    public GetMoviesResponseType getMovies(GetMoviesRequestType request) {
        int totalCount = request.count != null ? Math.max(0, request.count) : movies.size();

        int batchCount = totalCount / movies.size();
        int extraCount = totalCount % movies.size();

        List<MovieDto> retMovies = new ArrayList<MovieDto>();
        for (int i = 0; i < batchCount; ++i) {
            retMovies.addAll(movies);
        }
        retMovies.addAll(movies.subList(0, extraCount));

        return new GetMoviesResponseType(null, retMovies);
    }

    @Override
    public CheckHealthResponseType checkHealth(CheckHealthRequestType request) {
        return new CheckHealthResponseType();
    }
}
