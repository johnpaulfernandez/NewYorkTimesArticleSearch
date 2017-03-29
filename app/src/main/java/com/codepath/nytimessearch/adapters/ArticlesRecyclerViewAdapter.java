package com.codepath.nytimessearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.models.Article;
import com.codepath.nytimessearch.utils.Utils;

import java.util.List;

/**
 * Created by John on 3/25/2017.
 */

public class ArticlesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Store a member variable for the articles
    private List<Article> mArticles;
    // Store the context for easy access
    private Context mContext;

    private final int NO_IMAGE = 0, WITH_IMAGE = 1;
    String wideImageUrl;


    /***** Creating OnItemClickListener *****/
    // Define listener member variable
    public static OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Pass in the article array into the constructor
    public ArticlesRecyclerViewAdapter(Context context, List<Article> articles) {
        mArticles = articles;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        // Return a new holder instance
        switch (viewType) {
            case NO_IMAGE:
                View v1 = inflater.inflate(R.layout.item_article_no_image, parent, false);
                viewHolder = new ViewHolderNoImage(v1);
                break;
            case WITH_IMAGE:
                View v2 = inflater.inflate(R.layout.item_article_with_image, parent, false);
                viewHolder = new ViewHolderWithImage(v2);
                break;
            default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                viewHolder = new ViewHolderWithImage(v);
                break;
        }
        return viewHolder;
    }

    /**
     * This method internally calls onBindViewHolder(ViewHolder, int) to update the
     * RecyclerView.ViewHolder contents with the item at the given position
     * and also sets up some private fields to be used by RecyclerView.
     *
     * @param viewHolder The type of RecyclerView.ViewHolder to populate
     * @param position Item position in the viewgroup.
     */
    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {
            case NO_IMAGE:
                ViewHolderNoImage vh1 = (ViewHolderNoImage) viewHolder;
                configureViewHolderNoImage(vh1, position);
                break;
            case WITH_IMAGE:
                ViewHolderWithImage vh2 = (ViewHolderWithImage) viewHolder;
                configureViewHolderWithImage(vh2, position);
                break;
        }

        // Setup the click listener
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Triggers click upwards to the adapter on click
                if (listener != null) {
                    int position = viewHolder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(viewHolder.itemView, position);
                    }
                }
            }
        });
    }


    private void configureViewHolderNoImage(ViewHolderNoImage vh1, int position) {
        // Get the data model based on position
        Article article = mArticles.get(position);

        if (article != null) {
            vh1.getHeadline().setText(article.getHeadline());
            vh1.getLeadParagraph().setText(article.getLeadParagraph());
        }
    }

    private void configureViewHolderWithImage(ViewHolderWithImage vh2, int position) {
        // Get the data model based on position
        Article article = mArticles.get(position);

        if (article != null) {
            vh2.getHeadline().setText(article.getHeadline());
            vh2.getLeadParagraph().setText(article.getLeadParagraph());

            vh2.getImage().getLayoutParams().height = getScaledHeight(mContext);

            if (!TextUtils.isEmpty(wideImageUrl)) {
                Glide.with(mContext).load(wideImageUrl).into(vh2.getImage());
            }
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (mArticles.get(position).getMultimedia() != null) {
            wideImageUrl = mArticles.get(position).getWideImage();

            if (wideImageUrl != null)
                return WITH_IMAGE;
        }
        return NO_IMAGE;

    }

    private int getScaledHeight(Context context) {
        int totalWidth = Utils.getDisplayMetrics(context).widthPixels;
        return ((totalWidth / 2) / 16) * 9;
    }

}
