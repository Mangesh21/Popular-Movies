package movies.udacity.com.popularmovies;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import movies.udacity.com.popularmovies.database.MovieDetailsHelper;
import movies.udacity.com.popularmovies.network.MovieDetail;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MovieDetailActivity extends AppCompatActivity {

    TextView txtReleaseDate;
    TextView txtRatings;
    TextView txtMovieName;
    TextView txtMovieDetails;
    ImageView imgMovie;
    ImageView imgLike;

    MovieDetail movieDetail = null;

    SQLiteDatabase mSQLiteDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movieDetail = getIntent().getParcelableExtra(Constants.MOVIE_DETAILS);
        MovieDetailsHelper movieDetailsHelper = new MovieDetailsHelper(this);
        mSQLiteDatabase = movieDetailsHelper.getWritableDatabase();
        initView();

    }

    private void initView() {
        txtReleaseDate = (TextView) findViewById(R.id.txtreleasedate);
        txtRatings = (TextView) findViewById(R.id.txtratings);
        txtMovieDetails = (TextView) findViewById(R.id.txtmoviedetails);
        txtMovieName = (TextView) findViewById(R.id.txtmoviename);
        imgMovie = (ImageView) findViewById(R.id.imgmovie);
        imgLike = (ImageView) findViewById(R.id.imglike);

        txtReleaseDate.setText(movieDetail.getReleaseDate());
        SpannableString ratingsText = SpannableString.valueOf(movieDetail.getVoteAverage() + "/10");
        ratingsText.setSpan(new RelativeSizeSpan(1.7f), 0, String.valueOf(movieDetail.getVoteAverage()).length(), 0);
        txtRatings.setText(ratingsText);
        txtMovieDetails.setText(movieDetail.getOverview());
        txtMovieName.setText(movieDetail.getTitle());
        Glide.with(MovieDetailActivity.this).load(Utils.getCompleteImageURL(movieDetail.getPosterPath())).into(imgMovie);

        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cupboard().withDatabase(mSQLiteDatabase).put(movieDetail);

                Toast.makeText(MovieDetailActivity.this, "added to favourite", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
