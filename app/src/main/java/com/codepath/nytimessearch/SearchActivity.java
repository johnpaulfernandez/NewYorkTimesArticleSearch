package com.codepath.nytimessearch;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.nytimessearch.adapters.ArticleArrayAdapter;
import com.codepath.nytimessearch.models.Article;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.R.attr.filter;
import static android.R.attr.format;
import static com.codepath.nytimessearch.FilterActivity.beginDate;

public class SearchActivity extends AppCompatActivity {

    EditText etQuery;
    Button btnSearch;
    GridView gvResults;

    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;

    // Store a member variable for the listener
    private EndlessScrollListener scrollListener;
    private static int pageNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupViews();
        setUpEndlessScroll();
    }

    private void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        gvResults = (GridView) findViewById(R.id.gvResults);

        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onArticleSearch(View view) {

        pageNum = 0;

        // 1. First, clear the array of data
        articles.clear();
        // 2. Notify the adapter of the update
        adapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
        // 3. Reset endless scroll listener when performing a new search
        scrollListener.resetState();

        sendAPIRequest();
    }

    private void sendAPIRequest() {

        String query = etQuery.getText().toString();

        //Toast.makeText(this, "Searching for " + query, Toast.LENGTH_SHORT).show();

        AsyncHttpClient client = new AsyncHttpClient();

        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();

        params.put("page", pageNum++);
        params.put("q", query);

        params.put("sort", (FilterActivity.sortOrderIndex == 0 ? "oldest" : "newest"));

        if (beginDate != null)
            params.put("begin_date", beginDate);

        if (FilterActivity.bArts || FilterActivity.bFashion || FilterActivity.bSports) {
            String news_desk = "news_desk:(";
            ;

            if (FilterActivity.bArts) {
                news_desk += "\"Arts\"";
            }
            if (FilterActivity.bFashion) {
                if (FilterActivity.bArts)
                    news_desk += " ";
                news_desk += "\"Fashion & Style\"";
            }
            if (FilterActivity.bSports) {
                if (FilterActivity.bArts || FilterActivity.bFashion)
                    news_desk += " ";
                news_desk += "\"Sports\"";
            }

            params.put("fq", news_desk + ")");
        }


        params.put("api-key", "1ae65aff108b4882a37d1f0131eb3039");

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.d("DEBUG", response.toString());

                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONObject("response").getJSONArray("docs");
                    adapter.addAll(Article.articlesFromJSONArray(jsonArray));
                    Log.d("DEBUG", articles.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

        onViewContents();
    }

    public void launchFilterActivity(MenuItem item) {

        Intent intent = new Intent(SearchActivity.this, FilterActivity.class);
        startActivity(intent);

    }

    public void onViewContents() {

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Get the data item for position
                Article article = adapter.getItem(position);

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getWebUrl()));

                if (browserIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(browserIntent);
                }
            }
        });
    }

    public void setUpEndlessScroll() {

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
                //                // or loadNextDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        };

        // Attach the listener to the AdapterView onCreate
        gvResults.setOnScrollListener(scrollListener);

        // Adds the scroll listener to RecyclerView
        //rvItems.addOnScrollListener(scrollListener);
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyDataSetChanged()`
        sendAPIRequest();
    }
}
