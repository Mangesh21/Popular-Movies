package movies.udacity.com.popularmovies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import movies.udacity.com.popularmovies.network.APICallBack;
import movies.udacity.com.popularmovies.network.APIError;
import movies.udacity.com.popularmovies.network.BaseClient;
import movies.udacity.com.popularmovies.network.MovieList;
import movies.udacity.com.popularmovies.uiutils.MovieDetailAdapter;
import movies.udacity.com.popularmovies.uiutils.SpacesItemDecoration;

public class MainActivity extends AppCompatActivity {

    BaseClient baseClient = null;

    RecyclerView mRecyclerView;
    MovieDetailAdapter adapter;

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
        baseClient = new BaseClient();
        baseClient.init();
        getMovieList(Constants.ORDER_POPULARITY);
    }

    /**
     * this function will fecth the list of movies
     *
     * @param order
     */
    private void getMovieList(final String order) {

        baseClient.getMoviesList(order, new APICallBack<MovieList>() {
            @Override
            public void success(MovieList movieList) {
                if (adapter == null) {
                    adapter = new MovieDetailAdapter(MainActivity.this, movieList);
                    mRecyclerView.setAdapter(adapter);
                } else {
                    adapter.setMovieList(movieList);
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
        }

        if (id == R.id.action_sort_by_ratings) {
            if (!Constants.isOrderedByPopularity) {
                return true;
            } else {
                getMovieList(Constants.ORDER_RATING);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}