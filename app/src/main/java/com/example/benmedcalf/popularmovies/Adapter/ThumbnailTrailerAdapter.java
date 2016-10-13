package com.example.benmedcalf.popularmovies.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.benmedcalf.popularmovies.Model.Result;
import com.example.benmedcalf.popularmovies.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ben.medcalf on 10/6/16.
 */

public class ThumbnailTrailerAdapter extends BaseAdapter implements YouTubeThumbnailView.OnInitializedListener {

    private List<Result> mList;
    private Context mContext;
//    private int mLayoutId;
    public Map<View, YouTubeThumbnailLoader> mLoaders;



    public ThumbnailTrailerAdapter(final Context context, final List<Result> list) {
        mList = list;
        mContext = context;
        mLoaders = new HashMap<>();



    }

    @Override
    public int getCount() {

        return mList.size();
    }

    @Override
    public Object getItem(int position) {

        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        VideoHolder holder;
        final String videokey = mList.get(position).getKey();

        if (convertView == null) {
            // Create the row
            final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_trailers, parent, false);

            // Create the video holder
            holder = new VideoHolder();

            // Initialize the thumbnail
            holder.thumb = (YouTubeThumbnailView) convertView.findViewById(R.id.trailer_grid_item);

            //Logging what the videokey is here
            Log.d(this.toString(), "videokey is" + videokey);
            holder.thumb.setTag(videokey);


            holder.thumb.initialize("AIzaSyBpZu7TdfrT8DS9sCtihH2Y7Nozl1wWRyk", this);

            Log.d(this.toString(), "Tagging convertview with holder with view that has key " + holder.thumb.getTag());
            convertView.setTag(holder);
         } else {
            // Create it again
            Log.d(this.toString(), "creating holder from convertviews tag (I've hit else block)");
            holder = (VideoHolder) convertView.getTag();
            final YouTubeThumbnailLoader loader = mLoaders.get(holder.thumb);


            if (loader == null) {
                // Loader is currently initializing
                Log.d(this.toString(), "Loader is initializing still, setting videokey tag on holder's thumb. Key is " + videokey);
                holder.thumb.setTag(videokey);
                Log.d(this.toString(), "Now I'm getting tag from holder.thumb" + holder.thumb.getTag());
            } else {
                // The loader is already initialized
                // Note that its impossible to get a DeadObjectException here
                try {
                    Log.d(this.toString(), "loader.setVideo(videokey) in try block?");
                    loader.setVideo(videokey);
                } catch (IllegalStateException exception) {
                    //If the Loader has been released then remove it from the map and re-init
                    mLoaders.remove(holder.thumb);
                    holder.thumb.initialize("AIzaSyBpZu7TdfrT8DS9sCtihH2Y7Nozl1wWRyk", this);
                }
            }
        }
        return convertView;
    }

    @Override
    public boolean isEmpty() {
        return mList == null || mList.size() == 0;
    }

    @Override
    public void onInitializationSuccess(YouTubeThumbnailView view, final YouTubeThumbnailLoader loader) {
        mLoaders.put(view, loader);
        loader.setVideo(view.getTag().toString());
        loader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                loader.release();
            }

            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

            }
        });

    }

    @Override
    public void onInitializationFailure(YouTubeThumbnailView thumbnailView, YouTubeInitializationResult errorReason) {
        final String errorMessage = errorReason.toString();
        Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();


    }

    static class VideoHolder {
        YouTubeThumbnailView thumb;
    }
}
