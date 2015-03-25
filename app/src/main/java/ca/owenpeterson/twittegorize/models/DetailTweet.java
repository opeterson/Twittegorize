package ca.owenpeterson.twittegorize.models;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owen on 3/25/2015.
 */
public class DetailTweet {

    private long tweetId;
    private String body;
    private boolean favorited;
    private boolean retweeted;
    private String createdDate;
    private User user;
    private List<URL> urls;

    public DetailTweet() {

    }

    public DetailTweet(long tweetId, String body, String createdDate, User user, List<URL> urls ) {
        super();
        this.tweetId = tweetId;
        this.body = body;
        this.createdDate = createdDate;
        this.user = user;
        this.urls = urls;
    }

    public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public long getTweetId() {
        return tweetId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public static DetailTweet fromJson(JSONObject jsonObject) {
        DetailTweet tweet = new DetailTweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.tweetId = jsonObject.getLong("id");
            tweet.favorited = jsonObject.getBoolean("favorited");
            tweet.retweeted = jsonObject.getBoolean("retweeted");
            tweet.createdDate = jsonObject.getString("created_at");
            tweet.user = User.queryOrCreateUser(jsonObject.getJSONObject("user"));
            tweet.urls = DetailTweet.createURLList(jsonObject.getJSONObject("entities"));
        } catch (JSONException e) {
            Log.e("ERROR", e.getMessage());
            return null;
        }
        return tweet;
    }

    private static List<URL> createURLList(JSONObject entities) {

        JSONArray urls = null;
        List<URL> tweetURLs = new ArrayList<>();

        try {
            urls = entities.getJSONArray("urls");
            Log.d("URLS", urls.toString());
        } catch (JSONException ex) {
            Log.e("DetailTweet", "No entity found for 'urls' \n" + ex.getMessage());
        }

        if (null != urls) {
            for (int i = 0; i < urls.length(); i++) {
                JSONObject urlElement = null;
                String expandedURL = "";
                try {
                    urlElement = urls.getJSONObject(i);
                    expandedURL = urlElement.getString("expanded_url");
                } catch (JSONException ex) {
                    Log.e("DetailTweet", "Could not extract URLs from tweet. \n" + ex.getMessage());
                }

                if (StringUtils.isNotBlank(expandedURL)) {
                    URL url = null;
                    try {
                        url = new URL(expandedURL);
                    } catch (MalformedURLException ex) {
                        Log.e("DetailTweet", "URL is not in the correct format. \n" + ex.getMessage());
                    }

                    //if the URL object was created successfully, add it to the list.
                    if (null != url) {
                        tweetURLs.add(url);
                    }
                }
            }
        }

        return tweetURLs;
    }

//    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
//        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
//
//        for (int i=0; i < jsonArray.length(); i++) {
//            JSONObject tweetJson = null;
//            try {
//                tweetJson = jsonArray.getJSONObject(i);
//            } catch (Exception e) {
//                Log.e("TWEET PARSE ERROR", e.getMessage());
//                continue;
//            }
//
//            Tweet tweet = Tweet.fromJson(tweetJson);
//            if (tweet != null) {
//                tweets.add(tweet);
//            }
//        }
//
//        return tweets;
//    }
}