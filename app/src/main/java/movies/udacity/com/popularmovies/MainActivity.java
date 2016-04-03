package movies.udacity.com.popularmovies;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import movies.udacity.com.popularmovies.database.MovieDetailContentProvider;
import movies.udacity.com.popularmovies.network.APICallBack;
import movies.udacity.com.popularmovies.network.APIError;
import movies.udacity.com.popularmovies.network.BaseClient;
import movies.udacity.com.popularmovies.network.MovieDetail;
import movies.udacity.com.popularmovies.network.MovieList;
import movies.udacity.com.popularmovies.uiutils.MovieDetailAdapter;
import movies.udacity.com.popularmovies.uiutils.SpacesItemDecoration;

public class MainActivity extends AppCompatActivity {


    RecyclerView mRecyclerView;
    MovieDetailAdapter adapter;

    String[] projection = {"id", "title", "poster_path", "vote_average", "release_date", "overview",
            "review1", "review2", "movieTrailerOneID", "movieTrailerTwoID"

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.app_name));
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.moivies_grid);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        SpacesItemDecoration decoration = new SpacesItemDecoration(10, 2);
        mRecyclerView.addItemDecoration(decoration);
        init();


    }

    private void init() {

        getMovieList(Constants.ORDER_POPULARITY);
    }

    /**
     * this function will fecth the list of movies
     *
     * @param order
     */
    private void getMovieList(final String order) {

        BaseClient.getInstance().getMoviesList(order, new APICallBack<MovieList>() {
            @Override
            public void success(MovieList movieList) {
                if (adapter == null) {
                    adapter = new MovieDetailAdapter(MainActivity.this, movieList.getResults());
                    mRecyclerView.setAdapter(adapter);
                } else {
                    adapter.setMovieDetailsList(movieList.getResults());
                    adapter.notifyDataSetChanged();
                }
                if (TextUtils.equals(order, Constants.ORDER_POPULARITY)) {
                    Constants.isOrderedByPopularity = true;
                } else {
                    Constants.isOrderedByPopularity = false;
                }
            }

            @Override
            public void error(APIError error) {
                Toast.makeText(MainActivity.this, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_by_popularity) {
            if (Constants.isOrderedByPopularity) {
                return true;
            } else {
                getMovieList(Constants.ORDER_POPULARITY);
            }

            Constants.isOrderByRatings = false;
            Constants.isOrderedByPopularity  = true;
        }

        if (id == R.id.action_sort_by_ratings) {
            if (Constants.isOrderByRatings) {
                return true;
            } else {
                getMovieList(Constants.ORDER_RATING);
            }

            isOfflineData = false;
            Constants.isOrderByRatings = true;
            Constants.isOrderedByPopularity  = false;
        }

        if (id == R.id.action_sort_by_favourite) {
            Cursor moviesList = MainActivity.this.getContentResolver().query(
                    MovieDetailContentProvider.CONTENT_URI, projection, null, null, null);
            List<MovieDetail> results = new ArrayList<MovieDetail>();
            while (moviesList.moveToNext()) {
                results.add(new MovieDetail(
                        moviesList.getInt(0),
                        moviesList.getString(1), moviesList.getString(2),
                        moviesList.getDouble(3), moviesList.getString(4),
                        moviesList.getString(5), moviesList.getString(6),
                        moviesList.getString(7), moviesList.getString(8),
                        moviesList.getString(9), 1
                ));
            }
            if (adapter == null) {
                adapter = new MovieDetailAdapter(MainActivity.this, results);
                mRecyclerView.setAdapter(adapter);
            } else {
                adapter.setMovieDetailsList(results);
                adapter.notifyDataSetChanged();
            }


            Constants.isOrderByRatings = false;
            Constants.isOrderedByPopularity  = false;
        }

        return super.onOptionsItemSelected(item);
    }
}