package movies.udacity.com.popularmovies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import movies.udacity.com.popularmovies.network.MovieDetail;

public class MovieDetailActivity extends AppCompatActivity {

    TextView txtReleaseDate;
    TextView txtRatings;
    TextView txtMovieName;
    TextView txtMovieDetails;
    ImageView imgMovie;

    MovieDetail movieDetail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movieDetail = getIntent().getParcelableExtra(Constants.MOVIE_DETAILS);
        initView();
    }

    private void initView() {
        txtReleaseDate = (TextView) findViewById(R.id.txtreleasedate);
        txtRatings = (TextView) findViewById(R.id.txtratings);
        txtMovieDetails = (TextView) findViewById(R.id.txtmoviedetails);
        txtMovieName = (TextView) findViewById(R.id.txtmoviename);
        imgMovie = (ImageView) findViewById(R.id.imgmovie);

        txtReleaseDate.setText(movieDetail.getReleaseDate());
        SpannableString ratingsText = SpannableString.valueOf(movieDetail.getVoteAverage() +"/10");
        ratingsText.setSpan(new RelativeSizeSpan(1.7f),0, String.valueOf(movieDetail.getVoteAverage()).length(), 0);
        txtRatings.setText(ratingsText);
        txtMovieDetails.setText(movieDetail.getOverview());
        txtMovieName.setText(movieDetail.getTitle());
        Glide.with(MovieDetailActivity.this).load(Utils.getCompleteImageURL(movieDetail.getPosterPath())).into(imgMovie);
    }

}
