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
    public void onBindViewHolder(final ReviewsHolder holder, int position) {

        final ReviewResult review = mReviewsList.get(position);
        final String content = review.getContent();
        holder.setResult(review);

        holder.mAuthorTextView.setText(review.getAuthor());
        holder.mReviewTextView.setText(content);

        // Got my answer for this here:
        // http://stackoverflow.com/questions/3528790/textview-getlinecount-always-0-in-android
        // TODO: Should probably make a "Read More" button and set ViewGone to T/F instead of setMaxLines
        holder.mReviewTextView.post(new Runnable() {
            @Override
            public void run() {
                int lineCnt = holder.mReviewTextView.getLineCount();
                if (lineCnt >= 10) {
                    holder.setExpandable(true);
                    holder.mReviewTextView.setMaxLines(10);
                    holder.mReviewTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (holder.mExpandable) {
                                holder.setExpandable(false);
                                holder.mReviewTextView.setMaxLines(Integer.MAX_VALUE);
                            } else {
                                holder.setExpandable(true);
                                holder.mReviewTextView.setMaxLines(10);
                            }
                        }
                    });
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return (mReviewsList == null) ? 0 : mReviewsList.size();
    }

    public class ReviewsHolder extends RecyclerView.ViewHolder {

        public TextView mAuthorTextView;
        public TextView mReviewTextView;
        public ReviewResult mReviewResult;
        public Boolean mExpandable;

        public ReviewsHolder(View itemview) {
            super(itemview);
            mAuthorTextView = (TextView) itemview.findViewById(R.id.review_author);
            mReviewTextView = (TextView) itemview.findViewById(R.id.review_text);
            mExpandable = false;
        }

        public void setResult(ReviewResult reviewResult) {
            this.mReviewResult = reviewResult;
        }

        public void setExpandable(Boolean mExpandable) {
            this.mExpandable = mExpandable;
        }

    }
}
