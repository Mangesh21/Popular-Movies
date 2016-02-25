package movies.udacity.com.popularmovies.uiutils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import movies.udacity.com.popularmovies.Constants;
import movies.udacity.com.popularmovies.MovieDetailActivity;
import movies.udacity.com.popularmovies.R;
import movies.udacity.com.popularmovies.Utils;
import movies.udacity.com.popularmovies.network.MovieList;

/**
 * Created by mangesh on 22/2/16.
 */

public class MovieDetailAdapter extends RecyclerView.Adapter<MovieDetailAdapter.MovieView> {

    private Context context;

    public void setMovieList(MovieList movieList) {
        this.movieList = movieList;
    }

    private MovieList movieList;


    public MovieDetailAdapter(Context context, MovieList movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public MovieView onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        MovieView movieView = new MovieView(layoutView);
        return movieView;
    }

    @Override
    public void onBindViewHolder(MovieView holder, int position) {
        Glide.with(context).load(Utils.getCompleteImageURL(movieList.getResults().get(position).getPosterPath())).into(holder.imageView);
        holder.textView.setText(movieList.getResults().get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return movieList.getResults().size();
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
            movieDetaiIntent.putExtra(Constants.MOVIE_DETAILS, movieList.getResults().get(getAdapterPosition()));
            context.startActivity(movieDetaiIntent);
        }
    }




}
