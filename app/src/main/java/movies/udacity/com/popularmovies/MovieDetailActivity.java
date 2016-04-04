package movies.udacity.com.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import movies.udacity.com.popularmovies.network.MovieDetail;

public class MovieDetailActivity extends AppCompatActivity {

    MovieDetail movieDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movieDetail = getIntent().getParcelableExtra(Constants.MOVIE_DETAILS);
        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, MovieDetailFragment.newInstance(movieDetail)).commit();
        }

    }

}
