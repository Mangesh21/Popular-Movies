package movies.udacity.com.popularmovies.uiutils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import movies.udacity.com.popularmovies.Constants;
import movies.udacity.com.popularmovies.MovieDetailActivity;
import movies.udacity.com.popularmovies.R;
import movies.udacity.com.popularmovies.Utils;
import movies.udacity.com.popularmovies.network.MovieDetail;

/**
 * Created by mangesh on 22/2/16.
 */

public class MovieDetailAdapter extends RecyclerView.Adapter<MovieDetailAdapter.MovieView> {

    private Context context;


    public void setMovieDetailsList(List<MovieDetail> movieDetails) {
        this.mMovieDetails = movieDetails;

    }


    private List<MovieDetail> mMovieDetails;


    public MovieDetailAdapter(Context context, List<MovieDetail> movieDetails) {
        this.context = context;
        this.mMovieDetails = movieDetails;

    }

    @Override
    public MovieView onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        MovieView movieView = new MovieView(layoutView);
        return movieView;
    }

    @Override
    public void onBindViewHolder(MovieView holder, int position) {

        Glide.with(context).load(Utils.getCompleteImageURL(mMovieDetails.get(position).getPosterPath())).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);
        holder.textView.setText(mMovieDetails.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return mMovieDetails.size();
    }


    class MovieView extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView;

        public MovieView(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img);
            textView = (TextView) itemView.findViewById(R.id.movie_name);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            //Log.d("POSITION **** ", String.valueOf(getAdapterPosition()));
            Intent movieDetaiIntent = new Intent(context, MovieDetailActivity.class);
            movieDetaiIntent.putExtra(Constants.MOVIE_DETAILS, mMovieDetails.get(getAdapterPosition()));
            context.startActivity(movieDetaiIntent);
        }
    }


}
