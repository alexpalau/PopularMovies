package com.alexpalau.popmovies.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alexpalau.popmovies.popularmovies.R;
import com.alexpalau.popmovies.popularmovies.utilities.Utilities;
import com.squareup.picasso.Picasso;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>{

//    private List<Movie> movieList;
    private Cursor mCursor;
    Context context;

    private final MoviesAdapterOnClickHandler mClickHandler;

    public interface MoviesAdapterOnClickHandler{
        void onClick(int movie);
    }


    public MoviesAdapter(Context context, MoviesAdapterOnClickHandler clickHandler){
        this.context = context;
        mClickHandler = clickHandler;
    }



    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutGridItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutGridItem,parent,false);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
//            Movie movie = movieList.get(position);
        mCursor.moveToPosition(position);
        String poster = mCursor.getString(Utilities.INDEX_IMAGE);
        String posterPath= Utilities.buildImageUrl(poster);
        Picasso.with(context).load(posterPath).into(holder.mMovieImageView);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null ) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }


    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView mMovieImageView;

        public MoviesAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = (ImageView) view.findViewById(R.id.grid_item_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            //Movie movie = movieList.get(adapterPosition);
            int idMovie = mCursor.getInt(Utilities.INDEX_MOVIE_ID);
            mClickHandler.onClick(idMovie);
        }
    }



}
