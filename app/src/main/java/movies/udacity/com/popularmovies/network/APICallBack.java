package movies.udacity.com.popularmovies.network;

/**
 * Created by mangesh on 22/2/16.
 */


public interface APICallBack<T> {

    void success(T t);

    void error(APIError error);
}