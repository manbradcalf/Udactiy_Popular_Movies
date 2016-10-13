package com.example.benmedcalf.popularmovies.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.benmedcalf.popularmovies.Model.Result;
import com.example.benmedcalf.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ben.medcalf on 10/6/16.
 */

public class ThumbnailTrailerAdapter extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private List<Result> mList;

    public ThumbnailTrailerAdapter(Context context, List<Result> results) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = results;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Result getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.item_trailers, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        final Result result = getItem(position);

        viewHolder = (ViewHolder) view.getTag();

        String thumbnail_URL = "http://img.youtube.com/vi/" + result.getKey() + "/0.jpg";
        Picasso.with(getContext()).load(thumbnail_URL).into(viewHolder.imageView);

        return view;
    }

    private static class ViewHolder {
        final ImageView imageView;

        ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.trailer_grid_item);
        }

    }
}
