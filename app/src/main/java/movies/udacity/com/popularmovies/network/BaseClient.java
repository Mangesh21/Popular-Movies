package movies.udacity.com.popularmovies.network;

import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import movies.udacity.com.popularmovies.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by mangesh on 19/12/15.
 * This is base class for API request and response
 */
public class BaseClient {


    private static BaseClient instance = null;

    protected static Retrofit builder = null;

    private static final String BASE_URL = "http://api.themoviedb.org/";

    public BaseClient() {

    }

    private static API api;


    public static BaseClient getInstance() {
        if (instance == null) {
            instance = new BaseClient();
            init();
            return instance;
        } else {
            return instance;
        }
    }

    public static synchronized void init() {


        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();


        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaredClass().equals(MovieDetail.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }

                })
                .create();

        builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = builder.create(API.class);
    }


    public void getMoviesList(final String order, final APICallBack<MovieList> apiCallBack) {

        Call<MovieList> callBack = api.getMoviesList(order, Constants.API_KEY);
        callBack.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (response.isSuccess()) {
                    Log.d("LIST IS  **", response.body().toString());
                    apiCallBack.success(response.body());
                } else {
                    sendError(apiCallBack, response);
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                sendError(apiCallBack, t);
            }
        });
    }

    public void getMovieTrailers(final String movieID, final APICallBack<MovieTrailers> movieTrailersAPICallBack) {
        Call<MovieTrailers> callBack = api.getMovieTrailers(movieID, Constants.API_KEY);
        callBack.enqueue(new Callback<MovieTrailers>() {
            @Override
            public void onResponse(Call<MovieTrailers> call, Response<MovieTrailers> response) {
                if (response.isSuccess()) {
                    movieTrailersAPICallBack.success(response.body());
                } else {
                    sendError(movieTrailersAPICallBack, response);
                }
            }

            @Override
            public void onFailure(Call<MovieTrailers> call, Throwable t) {
                sendError(movieTrailersAPICallBack, t);
            }
        });

    }

    public void getMovieReviews(final String movieID, final APICallBack<MovieReviews> reviewsAPICallBack) {
        Call<MovieReviews> callBack = api.getMovieReviews(movieID, Constants.API_KEY);
        callBack.enqueue(new Callback<MovieReviews>() {
            @Override
            public void onResponse(Call<MovieReviews> call, Response<MovieReviews> response) {
                if (response.isSuccess()) {
                    reviewsAPICallBack.success(response.body());
                } else {
                    sendError(reviewsAPICallBack, response);
                }
            }

            @Override
            public void onFailure(Call<MovieReviews> call, Throwable t) {
                sendError(reviewsAPICallBack, t);
            }
        });
    }

    protected void sendError(APICallBack callback, Throwable t) {
        if (callback != null) {
            callback.error(new APIError(t.getMessage()));
        }
    }

    protected <T> void sendError(APICallBack callback, Response<T> response) {
        if (callback != null) {

            if (response != null && response.errorBody() != null) {
                try {
                    String message = new String(response.errorBody().bytes());
                    callback.error(new APIError(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

