package ca.owenpeterson.twittegorize.models;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
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
    private List<URL> images;
    private List<String> hashTags;

    public DetailTweet() {

    }

    public DetailTweet(long tweetId, String body, String createdDate, User user, List<URL> urls, List<URL> images ) {
        super();
        this.tweetId = tweetId;
        this.body = body;
        this.createdDate = createdDate;
        this.user = user;
        this.urls = urls;
        this.images = images;
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

    public List<URL> getUrls() {
        return urls;
    }

    public void setUrls(List<URL> urls) {
        this.urls = urls;
    }

    public List<URL> getImages() {
        return images;
    }

    public void setImages(List<URL> images) {
        this.images = images;
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

            JSONObject entities = jsonObject.getJSONObject("entities");

            tweet.urls = DetailTweet.createURLList(entities);

            if (StringUtils.contains(entities.toString(), "media")) {
                tweet.images = DetailTweet.createImageList(entities);
            } else {
                tweet.images = Collections.emptyList();
            }

        } catch (JSONException e) {
            Log.e("ERROR", e.getMessage());
            return null;
        }
        return tweet;
    }

    private static List<URL> createImageList(JSONObject entities) {
        JSONArray images = null;
        List<URL> imagesURLs = new ArrayList<>();

        try {
            images = entities.getJSONArray("media");

        } catch (JSONException ex) {
            Log.e("DetailTweet", "No entity found for 'media' \n" + ex.getMessage());
        }

        if (null != images) {
            for (int i = 0; i < images.length(); i++) {
                JSONObject urlElement = null;
                String expandedURL = "";
                try {
                    urlElement = images.getJSONObject(i);
                    expandedURL = urlElement.getString("expanded_url");
                } catch (JSONException ex) {
                    Log.e("DetailTweet", "Could not extract Images from tweet. \n" + ex.getMessage());
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
                        imagesURLs.add(url);
                    }
                }
            }

        }
        return imagesURLs;
    }

    private static List<URL> createURLList(JSONObject entities) {

        JSONArray urls = null;
        List<URL> tweetURLs = new ArrayList<>();

        try {
            urls = entities.getJSONArray("urls");
        } catch (JSONException ex) {
            Log.e("DetailTweet", "No entity found for 'urls' \n" + ex.getMessage());
        }

        if (null != urls) {
            for (int i = 0; i < urls.length(); i++) {
                JSONObject urlElement;
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
}
