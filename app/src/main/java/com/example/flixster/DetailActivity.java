package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {
    public static final String youtube_api_key = "AIzaSyCwO59zRdEAROXH3FZL1o8kmalXyInsuqU";
    public static final String videosURL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    TextView detailTitle;
    TextView detailOverview;
    RatingBar ratingBar;
    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailTitle= findViewById(R.id.detailTitle);
        detailOverview= findViewById(R.id.detailOverview);
        ratingBar= findViewById(R.id.ratingBar);
        youTubePlayerView = findViewById(R.id.player);

        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        detailTitle.setText(movie.getTitle());
        detailOverview.setText(movie.getOverview());
        ratingBar.setRating(movie.getRating());

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(videosURL, movie.getId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    JSONArray results =  json.jsonObject.getJSONArray("results");
                    if(results.length() == 0){
                        return;
                    }
                   String YTkey = results.getJSONObject(0).getString("key");
                    initializeYoutube(YTkey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });

    }

    private void initializeYoutube(String YTkey) {
        youTubePlayerView.initialize(youtube_api_key, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("Deatail Activity","on Success");
                youTubePlayer.cueVideo(YTkey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("Deatail Activity","on Failure");
            }
        });
    }
}