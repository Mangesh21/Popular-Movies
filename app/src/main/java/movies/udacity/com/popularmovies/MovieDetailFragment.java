package movies.udacity.com.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import movies.udacity.com.popularmovies.database.MovieDetailsHelper;
import movies.udacity.com.popularmovies.network.APICallBack;
import movies.udacity.com.popularmovies.network.APIError;
import movies.udacity.com.popularmovies.network.BaseClient;
import movies.udacity.com.popularmovies.network.MovieDetail;
import movies.udacity.com.popularmovies.network.MovieReviews;
import movies.udacity.com.popularmovies.network.MovieTrailers;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;


public class MovieDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    TextView txtReleaseDate;
    TextView txtRatings;
    TextView txtMovieName;
    TextView txtMovieDetails;
    TextView review1;
    TextView review2;
    ImageView imgMovie;
    ImageView imgLike;

    ImageView trailer1;
    ImageView trailer2;

    LinearLayout trailerLayout1 = null;

    LinearLayout trailerLayout2 = null;

    MovieDetail movieDetail = null;

    SQLiteDatabase mSQLiteDatabase = null;

    MovieTrailers mMovieTrailers = null;

    MovieReviews mMovieReviews = null;


    private View view;

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
        view =  inflater.inflate(R.layout.fragment_movie_detail, container, false);

        if(movieDetail!=null) {
            initView();
            if (movieDetail.isOfflineData()) {
                updateReviewsUI();
                updateTrailersUI();
            } else {
                initTrailers();
                initReviews();
            }
        }
        return view;
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
        txtReleaseDate = (TextView) view.findViewById(R.id.txtreleasedate);
        txtRatings = (TextView) view.findViewById(R.id.txtratings);
        txtMovieDetails = (TextView) view.findViewById(R.id.txtmoviedetails);
        txtMovieName = (TextView) view.findViewById(R.id.txtmoviename);
        imgMovie = (ImageView) view.findViewById(R.id.imgmovie);
        imgLike = (ImageView) view.findViewById(R.id.imglike);
        trailer1 = (ImageView) view.findViewById(R.id.trailer1);
        trailer2 = (ImageView) view.findViewById(R.id.trailer2);
        review1 = (TextView) view.findViewById(R.id.review1);
        review2 = (TextView) view.findViewById(R.id.review2);
        trailerLayout1 = (LinearLayout) view.findViewById(R.id.trailersone);
        trailerLayout2 = (LinearLayout) view.findViewById(R.id.trailerstwo);
        trailerLayout2.setVisibility(View.GONE);
        txtReleaseDate.setText(movieDetail.getReleaseDate());
        SpannableString ratingsText = SpannableString.valueOf(movieDetail.getVoteAverage() + "/10");
        ratingsText.setSpan(new RelativeSizeSpan(1.7f), 0, String.valueOf(movieDetail.getVoteAverage()).length(), 0);
        txtRatings.setText(ratingsText);
        txtMovieDetails.setText(movieDetail.getOverview());
        txtMovieName.setText(movieDetail.getTitle());
        Glide.with(getActivity()).load(Utils.getCompleteImageURL(movieDetail.getPosterPath())).into(imgMovie);

        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cupboard().withDatabase(mSQLiteDatabase).put(movieDetail);

                Toast.makeText(getActivity(), "added to favourite", Toast.LENGTH_SHORT).show();
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
                    movieDetail.setReview1(mMovieReviews.getResults().get(0).getContent());//Save Review 1
                    if (mMovieReviews.getResults().size() > 1) {
                        movieDetail.setReview2(mMovieReviews.getResults().get(1).getContent());// Save Review 2
                    }
                }


                updateReviewsUI();
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
        review1.setText(movieDetail.getReview1());
        review2.setText(movieDetail.getReview2());
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
