package movies.udacity.com.popularmovies;

/**
 * Created by mangesh on 25/2/16.
 */
public class Utils {

    public static String getCompleteImageURL(String url) {
        String baseURL = "http://image.tmdb.org/t/p/w185";
        String completeURL = baseURL + url;
        return completeURL;
    }
}
