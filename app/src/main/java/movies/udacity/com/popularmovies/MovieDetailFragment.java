package movies.udacity.com.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import movies.udacity.com.popularmovies.database.MovieDetailsHelper;
import movies.udacity.com.popularmovies.network.APICallBack;
import movies.udacity.com.popularmovies.network.APIError;
import movies.udacity.com.popularmovies.network.BaseClient;
import movies.udacity.com.popularmovies.network.MovieDetail;
import movies.udacity.com.popularmovies.network.MovieReviews;
import movies.udacity.com.popularmovies.network.MovieTrailers;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;


public class MovieDetailFragment extends Fragment {


    @Bind(R.id.txtreleasedate)
    TextView txtReleaseDate;
    @Bind(R.id.txtRelease)
    TextView txtRelease;
    @Bind(R.id.txtratings)
    TextView txtRatings;
    @Bind(R.id.txtmoviename)
    TextView txtMovieName;
    @Bind(R.id.txtmoviedetails)
    TextView txtMovieDetails;
    @Bind(R.id.txttrailers)
    TextView txttrailers;
    @Bind(R.id.reviews)
    TextView reviews;
    @Bind(R.id.imgmovie)
    ImageView imgMovie;
    @Bind(R.id.imglike)
    ImageView imgLike;
    @Bind(R.id.imgShare)
    ImageView imgShare;

    @Bind(R.id.trailer1)
    ImageView trailer1;
    @Bind(R.id.trailer2)
    ImageView trailer2;
    @Bind(R.id.trailersone)
    LinearLayout trailerLayout1;
    @Bind(R.id.trailerstwo)
    LinearLayout trailerLayout2;
    @Bind(R.id.reviewslayout)
    LinearLayout reviewsLayout;

    @Bind(R.id.viewStub)
    View viewstub;

    @Bind(R.id.viewStub2)
    View viewStub2;

    MovieDetail movieDetail = null;

    SQLiteDatabase mSQLiteDatabase = null;

    MovieTrailers mMovieTrailers = null;

    MovieReviews mMovieReviews = null;

    private View view;

    List<String> reviewsList = null;

    final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";

    final String SHARE_LINK = "Sharing Link...";

    public MovieDetailFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MovieDetailFragment newInstance(MovieDetail movieDetail) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.MOVIE_DETAILS, movieDetail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieDetail = getArguments().getParcelable(Constants.MOVIE_DETAILS);
            MovieDetailsHelper movieDetailsHelper = new MovieDetailsHelper(getActivity());
            mSQLiteDatabase = movieDetailsHelper.getWritableDatabase();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, view);
        if (movieDetail != null) {
            initView();
            if (movieDetail.isOfflineData() || movieDetail.isSavedInstanceData()) { // this is from saved instance
                if (movieDetail.isOfflineData()) { // isOfffine means from favourite
                    imgLike.setImageResource(R.drawable.like_selected);
                }
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<String>>() {
                }.getType();
                reviewsList = gson.fromJson(movieDetail.getReviewsJSON(), type);
                updateReviewsUI();
                updateTrailersUI();
            } else {
                MovieDetail detail = cupboard().withDatabase(mSQLiteDatabase).query(MovieDetail.class).withSelection("id = ?", String.valueOf(movieDetail.getId())).get();
                if (detail != null) { // this item is already in favourite list
                    imgLike.setImageResource(R.drawable.like_selected);
                }
                initTrailers();
                initReviews();
            }
        } else {
            hideViews();
        }
        return view;
    }

    private void hideViews() {
        txtRelease.setVisibility(View.GONE);
        imgLike.setVisibility(View.GONE);
        imgShare.setVisibility(View.GONE);
        txtMovieName.setVisibility(View.GONE);
        txtMovieDetails.setVisibility(View.GONE);
        txttrailers.setVisibility(View.GONE);
        trailerLayout1.setVisibility(View.GONE);
        trailerLayout2.setVisibility(View.GONE);
        reviews.setVisibility(View.GONE);
        viewstub.setVisibility(View.GONE);
        viewStub2.setVisibility(View.GONE);

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        movieDetail.setSavedInstanceData(1);
        outState.putParcelable(Constants.MOVIE_DETAILS, movieDetail);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void initView() {

        trailerLayout2.setVisibility(View.GONE);
        txtReleaseDate.setText(movieDetail.getReleaseDate());
        SpannableString ratingsText = SpannableString.valueOf(movieDetail.getVoteAverage() + "/10");
        ratingsText.setSpan(new RelativeSizeSpan(1.7f), 0, String.valueOf(movieDetail.getVoteAverage()).length(), 0);
        txtRatings.setText(ratingsText);
        txtMovieDetails.setText(movieDetail.getOverview());
        txtMovieName.setText(movieDetail.getTitle());
        Glide.with(getActivity()).load(Utils.getCompleteImageURL(movieDetail.getPosterPath()))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(imgMovie);

        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (movieDetail.isOfflineData()) { //its already from datavase
                    Toast.makeText(getActivity(), "This movie is already in your favourite list.", Toast.LENGTH_SHORT).show();

                } else {

                    MovieDetail detail = cupboard().withDatabase(mSQLiteDatabase).query(MovieDetail.class).withSelection("id = ?", String.valueOf(movieDetail.getId())).get();

                    if (detail == null) { //add new item only if its not in database
                        cupboard().withDatabase(mSQLiteDatabase).put(movieDetail);
                        imgLike.setImageResource(R.drawable.like_selected);
                        Toast.makeText(getActivity(), "added to favourite", Toast.LENGTH_SHORT).show();
                    } else { //already added in database.
                        Toast.makeText(getActivity(), "This movie is already in your favourite list.", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });

        trailer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movieDetail.getMovieTrailerOneID() != null) {
                    watchYoutubeVideo(movieDetail.getMovieTrailerOneID());//play movie clip
                }
            }
        });

        trailer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movieDetail.getMovieTrailerTwoID() != null) {
                    watchYoutubeVideo(movieDetail.getMovieTrailerTwoID());//play mvoie trailer
                }
            }
        });

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movieDetail.getMovieTrailerOneID() != null) {
                    shareTrailer();
                } else {
                    Toast.makeText(getActivity(), "No trailer found for this movie..", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void initTrailers() {
        BaseClient.getInstance().getMovieTrailers(String.valueOf(movieDetail.getId()), new APICallBack<MovieTrailers>() {
            @Override
            public void success(MovieTrailers movieTrailers) {
                mMovieTrailers = movieTrailers;
                int size = mMovieTrailers.getResults().size();
                if (size > 0) {
                    movieDetail.setMovieTrailerOneID(mMovieTrailers.getResults().get(0).getKey()); //save the trailer One ID
                    if (size > 1) {
                        movieDetail.setMovieTrailerTwoID(mMovieTrailers.getResults().get(1).getKey());// save the trailer Two ID
                    }
                }

                updateTrailersUI();
            }

            @Override
            public void error(APIError error) {
                Toast.makeText(getActivity(), error.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initReviews() {
        BaseClient.getInstance().getMovieReviews(String.valueOf(movieDetail.getId()), new APICallBack<MovieReviews>() {
            @Override
            public void success(MovieReviews movieReviews) {
                mMovieReviews = movieReviews;
                if (mMovieReviews.getResults().size() > 0) {

                    reviewsList = new ArrayList<String>();
                    for (int i = 0; i < mMovieReviews.getResults().size(); i++) {
                        reviewsList.add(mMovieReviews.getResults().get(i).getContent());
                    }

                    if (reviewsList != null && reviewsList.size() > 0) {
                        Gson gson = new Gson();

                        String inputString = gson.toJson(reviewsList);
                        movieDetail.setReviewsJSON(inputString);
                    } //add reviews JSON to MovieDetail object
                    updateReviewsUI();
                }


            }

            ;

            @Override
            public void error(APIError error) {
                Toast.makeText(getActivity(), error.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateTrailersUI() {

        if (movieDetail.getMovieTrailerOneID() == null && movieDetail.getMovieTrailerTwoID() == null) {
            trailerLayout1.setVisibility(View.GONE);
            trailerLayout2.setVisibility(View.GONE);
        } else if (movieDetail.getMovieTrailerTwoID() == null) {
            trailerLayout2.setVisibility(View.GONE);
        } else {
            trailerLayout2.setVisibility(View.VISIBLE);
        }
    }

    private void updateReviewsUI() {


        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout.LayoutParams lparams = null;
        if (reviewsList != null) {
            int size = reviewsList.size();
            for (int i = 0; i < size; i++) {
                TextView review = (TextView) inflater.inflate(R.layout.review_item, null);
                review.setText(reviewsList.get(i));
                reviewsLayout.addView(review); //add review
                if (lparams == null) {
                    lparams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, 3);
                    lparams.setMargins(15, 5, 0, 5);
                }
                View view = new View(getActivity());
                view.setLayoutParams(lparams);
                view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                reviewsLayout.addView(view);
            }
        }

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
                    Uri.parse(YOUTUBE_URL + id));
            startActivity(intent);
        }
    }

    public void shareTrailer() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,
                movieDetail.getTitle() + " Watch : " + YOUTUBE_URL + movieDetail.getMovieTrailerOneID());
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, SHARE_LINK));
    }


}
