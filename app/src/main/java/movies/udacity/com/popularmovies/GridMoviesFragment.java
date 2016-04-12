package movies.udacity.com.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GridMoviesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GridMoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GridMoviesFragment extends Fragment implements MovieDetailAdapter.OnMovieClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    RecyclerView mRecyclerView;

    MovieDetailAdapter adapter;

    private View mView;

    ArrayList<MovieDetail> mMovieDetailList = null;

    String[] projection = {"id", "title", "poster_path", "vote_average", "release_date", "overview", "reviewsJSON",
            "movieTrailerOneID", "movieTrailerTwoID"

    };

    public GridMoviesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GridMoviesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GridMoviesFragment newInstance(String param1, String param2) {
        GridMoviesFragment fragment = new GridMoviesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.MOVIE_LIST)) {
            mMovieDetailList = savedInstanceState.getParcelableArrayList(Constants.MOVIE_LIST);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_grid_movies, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.moivies_grid);
        initRecyclerView();
        if (mMovieDetailList == null) {
            getMovieList(Constants.ORDER_POPULARITY);
        } else {
            updateRecyclerViewFromSavedData();
        }
        return mView;
    }

    private void getMovieList(final String order) {
        BaseClient.getInstance().getMoviesList(order, new APICallBack<MovieList>() {
            @Override
            public void success(MovieList movieList) {
                mMovieDetailList = (ArrayList<MovieDetail>) movieList.getResults();
                if (adapter == null) {
                    adapter = new MovieDetailAdapter(getActivity(), GridMoviesFragment.this, mMovieDetailList);
                    mRecyclerView.setAdapter(adapter);
                } else {
                    adapter.setMovieDetailsList(mMovieDetailList);
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
                Toast.makeText(getActivity(), error.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        SpacesItemDecoration decoration = new SpacesItemDecoration(10, 2);
        mRecyclerView.addItemDecoration(decoration);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constants.MOVIE_LIST, mMovieDetailList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_by_favourite:

                Cursor moviesList = getActivity().getContentResolver().query(
                        MovieDetailContentProvider.CONTENT_URI, projection, null, null, null);
                List<MovieDetail> results = new ArrayList<MovieDetail>();
                while (moviesList.moveToNext()) {
                    results.add(new MovieDetail(
                            moviesList.getInt(0),
                            moviesList.getString(1), moviesList.getString(2),
                            moviesList.getDouble(3), moviesList.getString(4),
                            moviesList.getString(5), moviesList.getString(6),
                            moviesList.getString(7), moviesList.getString(8), 1
                    ));
                }
                mMovieDetailList = (ArrayList<MovieDetail>) results;
                if (adapter == null) {
                    adapter = new MovieDetailAdapter(getActivity(), GridMoviesFragment.this, mMovieDetailList);
                    mRecyclerView.setAdapter(adapter);
                } else {
                    adapter.setMovieDetailsList(results);
                    adapter.notifyDataSetChanged();
                }

                Constants.isOrderByRatings = false;
                Constants.isOrderedByPopularity = false;
                return true;
            case R.id.action_sort_by_popularity:
                if (Constants.isOrderedByPopularity) {
                    return true;
                } else {
                    getMovieList(Constants.ORDER_POPULARITY);
                }

                Constants.isOrderByRatings = false;
                Constants.isOrderedByPopularity = true;
                return true;
            case R.id.action_sort_by_ratings:
                if (Constants.isOrderByRatings) {
                    return true;
                } else {
                    getMovieList(Constants.ORDER_RATING);
                }

                Constants.isOrderByRatings = true;
                Constants.isOrderedByPopularity = false;
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieClicked(MovieDetail movieDetail) {
        mListener.onFragmentInteraction(movieDetail);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(MovieDetail movieDetail);
    }

    public void updateRecyclerViewFromSavedData() {
        adapter = new MovieDetailAdapter(getActivity(), GridMoviesFragment.this, mMovieDetailList);
        mRecyclerView.setAdapter(adapter);
    }
}
