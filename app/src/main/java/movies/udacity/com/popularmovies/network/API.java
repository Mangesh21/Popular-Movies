package movies.udacity.com.popularmovies.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

}



