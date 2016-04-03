package movies.udacity.com.popularmovies.network;

/**
 * Created by mangesh on 3/4/16.
 */

import java.util.ArrayList;
import java.util.List;

public class MovieTrailers {

    private int id;
    private List<Result> results = new ArrayList<Result>();

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }

}


