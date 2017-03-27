package com.alexpalau.popmovies.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexpalau.popmovies.popularmovies.R;
import com.alexpalau.popmovies.popularmovies.utilities.Utilities;

/**
 * Created by Alex on 05/02/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder>{

//    private List<Review> reviewList;
    private Cursor mCursorReview;
    Context context;

    private final ReviewsAdapterOnClickHandler mClickHandler;

    public interface ReviewsAdapterOnClickHandler{
        void onClick(String urlReview);
    }


    public ReviewsAdapter(Context context, ReviewsAdapterOnClickHandler clickHandler){
        this.context = context;
        mClickHandler = clickHandler;
    }



    @Override
    public ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutGridItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutGridItem,parent,false);
        return new ReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapterViewHolder holder, int position) {
        mCursorReview.moveToPosition(position);

        holder.mAuthor.setText(mCursorReview.getString(Utilities.INDEX_REVIEW_AUTHOR));
        holder.mContent.setText(mCursorReview.getString(Utilities.INDEX_REVIEW_CONTENT));
    }

    @Override
    public int getItemCount() {
        if (mCursorReview == null ) return 0;
        return mCursorReview.getCount();
    }

//    public void setReviewsData(List<Review> reviewsData) {
//        if (reviewList!=null) reviewList.clear();
//        reviewList = reviewsData;
//        notifyDataSetChanged();
//    }

    public void swapCursor(Cursor newCursor) {
        mCursorReview = newCursor;
        notifyDataSetChanged();
    }

    public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mAuthor;
        public final TextView mContent;

        public ReviewsAdapterViewHolder(View view) {
            super(view);
            mAuthor = (TextView)view.findViewById(R.id.tv_author);
            mContent = (TextView)view.findViewById(R.id.tv_content);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursorReview.moveToPosition(adapterPosition);
//            Review review = reviewList.get(adapterPosition);
            mClickHandler.onClick(mCursorReview.getString(Utilities.INDEX_REVIEW_URL));
        }
    }



}
