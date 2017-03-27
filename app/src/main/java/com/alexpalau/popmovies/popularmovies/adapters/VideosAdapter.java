package com.alexpalau.popmovies.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexpalau.popmovies.popularmovies.R;
import com.alexpalau.popmovies.popularmovies.utilities.Utilities;

/**
 * Created by Alex on 05/02/2017.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosAdapterViewHolder>{

//    private List<Video> videoList;
    Cursor mCursorVideo;
    Context context;


    private final VideosAdapterOnClickHandler mClickHandler;

    public interface VideosAdapterOnClickHandler{
        void onClick(String videoKey);
    }


    public VideosAdapter(Context context, VideosAdapterOnClickHandler clickHandler){
        this.context = context;
        mClickHandler = clickHandler;
    }



    @Override
    public VideosAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutGridItem = R.layout.video_item_c;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutGridItem,parent,false);
        return new VideosAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideosAdapterViewHolder holder, int position) {
        //Video video = videoList.get(position);
        mCursorVideo.moveToPosition(position);
        holder.mName.setText(mCursorVideo.getString(Utilities.INDEX_VIDEO_NAME));
        //holder.mVideo.setImageDrawable(R.drawable.ic_play_arrow_black_24px);
    }

    @Override
    public int getItemCount() {
        if (mCursorVideo == null ) return 0;
        return mCursorVideo.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursorVideo = newCursor;
        notifyDataSetChanged();
    }

//    public void setVideosData(List<Video> videosData) {
//        if (videoList!=null) videoList.clear();
//        videoList = videosData;
//        notifyDataSetChanged();
//    }

    public class VideosAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mName;
        public final ImageView mVideo;

        public VideosAdapterViewHolder(View view) {
            super(view);
            mName = (TextView)view.findViewById(R.id.tv_videoName);
            mVideo = (ImageView) view.findViewById(R.id.video_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursorVideo.moveToPosition(adapterPosition);
            //Video video = videoList.get(adapterPosition);
            mClickHandler.onClick(mCursorVideo.getString(Utilities.INDEX_VIDEO_KEY));
        }
    }



}
