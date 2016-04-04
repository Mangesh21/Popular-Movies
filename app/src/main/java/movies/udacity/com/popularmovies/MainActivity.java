package movies.udacity.com.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import movies.udacity.com.popularmovies.network.MovieDetail;
import movies.udacity.com.popularmovies.uiutils.MovieDetailAdapter;

public class MainActivity extends AppCompatActivity implements GridMoviesFragment.OnFragmentInteractionListener {


    // RecyclerView mRecyclerView;
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
        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of ExampleFragment
            GridMoviesFragment gridMoviesFragment = new GridMoviesFragment();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            gridMoviesFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, gridMoviesFragment).commit();
        }
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
     /*   int id = item.getItemId();

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
*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(MovieDetail movieDetail) {
        Intent movieDetaiIntent = new Intent(MainActivity.this, MovieDetailActivity.class);
        movieDetaiIntent.putExtra(Constants.MOVIE_DETAILS, movieDetail);
        startActivity(movieDetaiIntent);
    }
}