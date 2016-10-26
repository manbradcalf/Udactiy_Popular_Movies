package com.example.benmedcalf.popularmovies.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.benmedcalf.popularmovies.Model.ReviewResult;
import com.example.benmedcalf.popularmovies.R;

import java.util.List;

/**
 * Created by ben.medcalf on 10/26/16.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsHolder> {

    private List<ReviewResult> mReviewsList;
    private Context mContext;
    private LayoutInflater mInflater;

    public ReviewsAdapter(Context context, List<ReviewResult> reviewsList) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mReviewsList = reviewsList;
    }

    @Override
    public ReviewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) mInflater.inflate(R.layout.item_review, parent, false);
        return new ReviewsHolder(layout);
    }

    @Override
    public void onBindViewHolder(ReviewsHolder holder, int position) {

        final ReviewResult review = mReviewsList.get(position);
        holder.setResult(review);

        holder.mAuthorTextView.setText(review.getAuthor());
        holder.mReviewTextView.setText(review.getContent());

    }

    @Override
    public int getItemCount() {
        return (mReviewsList == null) ? 0 : mReviewsList.size();
    }

    public class ReviewsHolder extends RecyclerView.ViewHolder {

        public TextView mAuthorTextView;
        public TextView mReviewTextView;
        public ReviewResult mReviewResult;

        public ReviewsHolder(View itemview) {
            super(itemview);
            mAuthorTextView = (TextView) itemview.findViewById(R.id.review_author);
            mReviewTextView = (TextView) itemview.findViewById(R.id.review_text);
        }

        public void setResult(ReviewResult reviewResult) {
            this.mReviewResult = reviewResult;
        }

    }
}
