package com.necohorne.popularmovies.Utils.RecyclerAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.necohorne.popularmovies.Model.Review;
import com.necohorne.popularmovies.R;

import java.util.ArrayList;

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Review> mReviews;

    public ReviewRecyclerAdapter(ArrayList<Review> reviews, Context context) {
        mReviews = reviews;
        mContext = context;
    }

    @NonNull
    @Override
    public ReviewRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item_layout, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewRecyclerAdapter.ViewHolder holder, int position) {
        final Review review = mReviews.get(position);
        if(review != null){
            if(review.getAuthor() != null){
                holder.author.setText(review.getAuthor());
            } else {
                holder.author.setText(R.string.unknown);
            }

            if(review.getContent() != null){
                holder.reviewText.setText(review.getContent());
            } else {
                holder.reviewText.setText("");
            }
        }

    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView author;
        public TextView reviewText;

        public ViewHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.review_author_tv);
            reviewText = itemView.findViewById(R.id.review_content_tv);
        }
    }
}
