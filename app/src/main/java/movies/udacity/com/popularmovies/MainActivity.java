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

        return false;
    }

    @Override
    public void onFragmentInteraction(MovieDetail movieDetail) {
        Intent movieDetaiIntent = new Intent(MainActivity.this, MovieDetailActivity.class);
        movieDetaiIntent.putExtra(Constants.MOVIE_DETAILS, movieDetail);
        startActivity(movieDetaiIntent);
    }
}