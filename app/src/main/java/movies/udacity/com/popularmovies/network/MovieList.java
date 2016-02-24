package movies.udacity.com.popularmovies.network;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mangesh on 21/2/16.
 */


public class MovieList {

    private Integer page;
    private List<MovieDetail> results = new ArrayList<MovieDetail>();
    private Integer totalResults;
    private Integer totalPages;

    /**
     * @return The page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * @param page The page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * @return The results
     */
    public List<MovieDetail> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setResults(List<MovieDetail> results) {
        this.results = results;
    }

    /**
     * @return The totalResults
     */
    public Integer getTotalResults() {
        return totalResults;
    }

    /**
     * @param totalResults The total_results
     */
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    /**
     * @return The totalPages
     */
    public Integer getTotalPages() {
        return totalPages;
    }

    /**
     * @param totalPages The total_pages
     */
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public String toString() {
        return "MovieList{" +
                "page=" + page +
                ", results=" + results +
                ", totalResults=" + totalResults +
                ", totalPages=" + totalPages +
                '}';
    }
}
