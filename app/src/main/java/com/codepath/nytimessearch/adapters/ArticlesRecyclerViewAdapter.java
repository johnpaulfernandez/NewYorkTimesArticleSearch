package com.codepath.nytimessearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.models.Article;
import com.codepath.nytimessearch.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.R.attr.button;
import static android.R.attr.thumbnail;

/**
 * Created by John on 3/25/2017.
 */

public class ArticlesRecyclerViewAdapter extends RecyclerView.Adapter<ArticlesRecyclerViewAdapter.ViewHolder> {

    // Store a member variable for the articles
    private List<Article> mArticles;
    // Store the context for easy access
    private Context mContext;

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

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvTitle;
        public ImageView ivImage;
        public TextView tvLeadParagraph;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            tvLeadParagraph = (TextView) itemView.findViewById(R.id.tvLeadParagraph);

            // Setup the click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
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
    public ArticlesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View articleView = inflater.inflate(R.layout.item_article_result, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArticlesRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Article article = mArticles.get(position);

        // Set item views based on your views and data model
        TextView headline = viewHolder.tvTitle;
        TextView leadParagraph = viewHolder.tvLeadParagraph;

        headline.setText(article.getHeadline());
        leadParagraph.setText(article.getLeadParagraph());
        ImageView image = viewHolder.ivImage;

        image.getLayoutParams().height = getScaledHeight(mContext);

        String wideImageUrl;

        if (article.getMultimedia() != null) {
            wideImageUrl = article.getWideImage();

            if (!TextUtils.isEmpty(wideImageUrl)) {
                Glide.with(mContext).load(wideImageUrl).into(image);
            }
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    private int getScaledHeight(Context context) {
        int totalWidth = Utils.getDisplayMetrics(context).widthPixels;
        return ((totalWidth / 2) / 16) * 9;
    }
}
