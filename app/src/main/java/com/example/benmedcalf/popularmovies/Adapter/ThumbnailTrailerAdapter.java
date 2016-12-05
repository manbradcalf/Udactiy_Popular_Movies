package com.example.benmedcalf.popularmovies.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.benmedcalf.popularmovies.Model.VideoResult;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.example.benmedcalf.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ben.medcalf on 10/6/16.
 */

public class ThumbnailTrailerAdapter extends RecyclerView.Adapter<ThumbnailTrailerAdapter.TrailerHolder> {

    private List<VideoResult> mTrailerResultsList;
    private LayoutInflater mInflater;
    private Context mContext;
    public static final String BASE_URL_YOUTUBE_THUMB = "http://img.youtube.com/vi/";
    public static final String THUMB_SIZE_SUFFIX = "/0.jpg";

    public ThumbnailTrailerAdapter(Context context, List<VideoResult> videoResults) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mTrailerResultsList = videoResults;

    }

    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView = (ImageView) mInflater.inflate(R.layout.item_trailers, parent, false);
        return new TrailerHolder(imageView);

    }


    @Override
    public void onBindViewHolder(TrailerHolder holder, int position) {

        final VideoResult videoResult = mTrailerResultsList.get(position);
        holder.setResult(videoResult);

        Picasso.with(mContext)
                .load(BASE_URL_YOUTUBE_THUMB + videoResult.getKey() + THUMB_SIZE_SUFFIX)
                .placeholder(R.drawable.ic_thumb_background)
                .into(holder.mImageView);

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final YouTubeInitializationResult result = YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(mContext);

                if (result != YouTubeInitializationResult.SUCCESS) {
                    //If there are any issues we can show an error dialog.
                    Toast toast = Toast.makeText(mContext, "Can't play the video without the YouTube App!", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    Intent intent = YouTubeIntents.createPlayVideoIntent(mContext, videoResult.getKey());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mTrailerResultsList == null) ? 0 : mTrailerResultsList.size();
    }

    public class TrailerHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public VideoResult mVideoResult;

        public void setResult(VideoResult videoResult) {
            mVideoResult = videoResult;
        }

        public TrailerHolder(ImageView itemview) {
            super(itemview);
            mImageView = (ImageView) itemview.findViewById(R.id.trailer_grid_item);

        }

    }

}
