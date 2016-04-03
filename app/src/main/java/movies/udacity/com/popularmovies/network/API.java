package movies.udacity.com.popularmovies.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by mangesh on 20/12/15.
 * This is interface for popular movies app
 */
public interface API {

    @GET("3/discover/movie?")
    Call<MovieList> getMoviesList(@Query("sort_by") String sort, @Query("api_key") String apiKey);

    @GET("3/movie/{movieID}/videos")
    Call<MovieTrailers> getMovieTrailers(@Path("movieID") String movieID, @Query("api_key") String apiKey);
}



