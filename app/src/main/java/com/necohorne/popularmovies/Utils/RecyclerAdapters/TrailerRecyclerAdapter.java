package com.necohorne.popularmovies.Utils.RecyclerAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.necohorne.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.necohorne.popularmovies.Utils.Constants.THUMBNAIL_QUERY;
import static com.necohorne.popularmovies.Utils.Constants.YOUTUBE_IMAGE_URL;

public class TrailerRecyclerAdapter extends RecyclerView.Adapter<TrailerRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mLinks;

    public TrailerRecyclerAdapter(ArrayList<String> linkList, Context context) {
        mLinks = linkList;
        mContext = context;
    }

    @NonNull
    @Override
    public TrailerRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_list_item_layout, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerRecyclerAdapter.ViewHolder holder, int position) {

        final String youtubeUrl = mLinks.get(position);

        //get the video thumbnail and use picasso to load it into an image view.
        Uri uri = Uri.parse(youtubeUrl);
        String videoID = uri.getQueryParameter("v");
        String url = YOUTUBE_IMAGE_URL + videoID + THUMBNAIL_QUERY;
        Picasso.with(mContext)
                .load(url)
                .placeholder(R.drawable.ic_popcorn)
                .into(holder.trailerThumbnail);

        holder.trailerThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl));
                mContext.startActivity(Intent.createChooser( intent, "Open Video" ));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mLinks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView trailerThumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            trailerThumbnail = itemView.findViewById(R.id.thumbnail_iv);
        }
    }
}
