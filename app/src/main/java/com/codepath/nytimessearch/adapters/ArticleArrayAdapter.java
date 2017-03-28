package com.codepath.nytimessearch.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by John on 3/19/2017.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, R.layout.item_article_result, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for position
        Article article = this.getItem(position);

        // Check to see if existing view is being reused
        // If not using a recycled view (no available reincarnated view), inflate the layout
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
        }

        // Find the image
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);

        // Clear out recycled image from convertView from last time
        imageView.setImageResource(0);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(article.getHeadline());

        // Populate the thumbnail image
        // download and cache remote images in the background
        // Android doesn't come with a library out of the box; we need to use third party library called Picasso to do the remote image fetching
        String wideImageUrl = article.getWideImage();

        try {
            if (!TextUtils.isEmpty(wideImageUrl)) {
                Picasso.with(this.getContext()).load(wideImageUrl).fit().placeholder(R.mipmap.ic_launcher).into(imageView);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//
//        Picasso.Builder builder = new Picasso.Builder(this.getContext());
//        builder.listener(new Picasso.Listener() {
//            @Override
//            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
//                exception.printStackTrace();
//                Log.d("DEBUG", imageView.toString());
//            }
//        });

        return convertView;
    }
}
