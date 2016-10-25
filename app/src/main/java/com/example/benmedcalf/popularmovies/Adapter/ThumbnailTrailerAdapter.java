package com.example.benmedcalf.popularmovies.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.benmedcalf.popularmovies.Model.Result;
import com.example.benmedcalf.popularmovies.R;
import com.google.android.youtube.player.YouTubeIntents;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ben.medcalf on 10/6/16.
 */

public class ThumbnailTrailerAdapter extends RecyclerView.Adapter<ThumbnailTrailerAdapter.TrailerHolder>{

    private List<Result> mTrailerResultsList;
    private LayoutInflater mInflater;
    private Context mContext;
    public static final String BASE_URL_YOUTUBE_THUMB = "http://img.youtube.com/vi/";
    public static final String THUMB_SIZE_SUFFIX = "/0.jpg";

    public ThumbnailTrailerAdapter(Context context, List<Result> results) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mTrailerResultsList = results;

    }

    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView = (ImageView) mInflater.inflate(R.layout.item_trailers, parent, false);
        return new TrailerHolder(imageView);

    }

    @Override
    public void onBindViewHolder(TrailerHolder holder, int position) {

        final Result result = mTrailerResultsList.get(position);
        holder.setResult(result);

        Picasso.with(mContext)
                .load(BASE_URL_YOUTUBE_THUMB + result.getKey() + THUMB_SIZE_SUFFIX)
                .placeholder(R.drawable.ic_thumb_background)
                .into(holder.mImageView);

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = YouTubeIntents.createPlayVideoIntent(mContext, result.getKey());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mTrailerResultsList == null) ? 0 : mTrailerResultsList.size();
    }

    public class TrailerHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public Result mResult;

        public void setResult(Result result) {mResult = result;}

        public TrailerHolder(ImageView itemview) {
            super(itemview);
            mImageView = (ImageView) itemview.findViewById(R.id.trailer_grid_item);

        }

    }

}
