package ca.owenpeterson.twittegorize.models;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.owenpeterson.twittegorize.utils.JodaDateUtils;
import ca.owenpeterson.twittegorize.utils.UserUtil;

/**
 * Created by Owen on 3/25/2015.
 *
 * This class is used to display a tweet with more detail than is shown in the first list view.
 * URLS and Images are also added here, but are ignored in the Tweet class. This may change in
 * future versions.
 *
 * When the user clicks on an item in the list view of tweets, a detail screen is shown.
 * The activity makes a call to twitter for a specific tweet. The ID is passed to the activity from
 * the list.
 *
 * When the call to Twitter is finished, the data is parsed into this model.
 */
public class DetailTweet {

    private long tweetId;
    private String body;
    private boolean favorited;
    private boolean retweeted;
    private DateTime createdDate;
    private User user;
    private List<URL> urls;
    private List<URL> images;
    private List<String> hashTags;

    public DetailTweet() {
    }

    public DetailTweet(long tweetId, String body, DateTime createdDate, User user, List<URL> urls, List<URL> images ) {
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

    public DateTime getCreatedDate() {
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
            tweet.createdDate = JodaDateUtils.parseDateTime(jsonObject.getString("created_at"));
            tweet.user = UserUtil.queryOrCreateUser(jsonObject.getJSONObject("user"));

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

        //Log.d("ENTITIES VALUE:\n", entities.toString());

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
                    expandedURL = urlElement.getString("media_url");
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
