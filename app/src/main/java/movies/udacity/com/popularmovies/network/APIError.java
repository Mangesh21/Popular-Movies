package movies.udacity.com.popularmovies.network;

/**
 * Created by mangesh on 22/2/16..
 */


public class APIError {
    private String errorMessage = null;

    public APIError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}