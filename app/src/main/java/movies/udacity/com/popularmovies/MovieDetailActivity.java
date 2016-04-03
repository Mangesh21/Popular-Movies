package movies.udacity.com.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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
import movies.udacity.com.popularmovies.network.APICallBack;
import movies.udacity.com.popularmovies.network.APIError;
import movies.udacity.com.popularmovies.network.BaseClient;
import movies.udacity.com.popularmovies.network.MovieDetail;
import movies.udacity.com.popularmovies.network.MovieReviews;
import movies.udacity.com.popularmovies.network.MovieTrailers;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MovieDetailActivity extends AppCompatActivity {

    TextView txtReleaseDate;
    TextView txtRatings;
    TextView txtMovieName;
    TextView txtMovieDetails;
    ImageView imgMovie;
    ImageView imgLike;

    ImageView trailer1;
    ImageView trailer2;
    MovieDetail movieDetail = null;

    SQLiteDatabase mSQLiteDatabase = null;

    MovieTrailers mMovieTrailers = null;

    MovieReviews mMovieReviews = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movieDetail = getIntent().getParcelableExtra(Constants.MOVIE_DETAILS);
        MovieDetailsHelper movieDetailsHelper = new MovieDetailsHelper(this);
        mSQLiteDatabase = movieDetailsHelper.getWritableDatabase();
        initView();
        initTrailers();
        initReviews();
    }

    private void initReviews() {
        BaseClient.getInstance().getMovieReviews(String.valueOf(movieDetail.getId()), new APICallBack<MovieReviews>() {
            @Override
            public void success(MovieReviews movieReviews) {
                mMovieReviews = movieReviews;
                movieDetail.setReview1(mMovieReviews.getResults().get(0).getContent());//Save Review 1
                movieDetail.setReview2(mMovieReviews.getResults().get(1).getContent());// Save Review 2
            }

            ;

            @Override
            public void error(APIError error) {
                Toast.makeText(MovieDetailActivity.this, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initTrailers() {
        BaseClient.getInstance().getMovieTrailers(String.valueOf(movieDetail.getId()), new APICallBack<MovieTrailers>() {
            @Override
            public void success(MovieTrailers movieTrailers) {
                mMovieTrailers = movieTrailers;
                movieDetail.setMovieTrailerOneID(mMovieTrailers.getResults().get(0).getKey()); //save the trailer One ID
                movieDetail.setMovieTrailerTwoID(mMovieTrailers.getResults().get(1).getKey());// save the trailer Two ID
            }

            @Override
            public void error(APIError error) {
                Toast.makeText(MovieDetailActivity.this, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        txtReleaseDate = (TextView) findViewById(R.id.txtreleasedate);
        txtRatings = (TextView) findViewById(R.id.txtratings);
        txtMovieDetails = (TextView) findViewById(R.id.txtmoviedetails);
        txtMovieName = (TextView) findViewById(R.id.txtmoviename);
        imgMovie = (ImageView) findViewById(R.id.imgmovie);
        imgLike = (ImageView) findViewById(R.id.imglike);
        trailer1 = (ImageView) findViewById(R.id.trailer1);
        trailer2 = (ImageView) findViewById(R.id.trailer2);
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

        trailer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMovieTrailers != null) {
                    watchYoutubeVideo(mMovieTrailers.getResults().get(0).getKey());//play movie clip
                }
            }
        });

        trailer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMovieTrailers != null) {
                    watchYoutubeVideo(mMovieTrailers.getResults().get(1).getKey());//play mvoie trailer
                }
            }
        });


    }


    /**
     * Play video either in youtube or browser. Ref - http://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
     *
     * @param id
     */
    public void watchYoutubeVideo(String id) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            startActivity(intent);
        }
    }

}
