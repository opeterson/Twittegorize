package ca.owenpeterson.twittegorize.data;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ca.owenpeterson.twittegorize.listeners.FeedResponseParsed;
import ca.owenpeterson.twittegorize.models.Retweet;
import ca.owenpeterson.twittegorize.models.Tweet;
import ca.owenpeterson.twittegorize.utils.JodaDateUtils;
import ca.owenpeterson.twittegorize.utils.UserUtil;

/**
 * This is an async task that processes a json twitter response into tweets, retweets, users
 * and retweeted users in the database.
 * Created by owen on 7/27/15.
 */
public class TwitterFeedResponseParser extends AsyncTask<Void, Void, Void>{
    private RetweetDAO retweetDAO = new RetweetDAO();
    private TweetDAO tweetDAO = new TweetDAO();
    List<Tweet> tweets = new ArrayList<>(200);
    List<Retweet> retweets = new ArrayList<>(200);
    private JSONArray response;
    private FeedResponseParsed listener;

    public TwitterFeedResponseParser(JSONArray response) {
        this.response = response;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.OnFeedResponseParsed();
    }

    @Override
    protected Void doInBackground(Void... params) {
        populateListsFromJson(response);
        return null;
    }

    public void setOnFeedResponseParsedListener(FeedResponseParsed listener) {
        this.listener = listener;
    }

    private void populateListsFromJson(JSONArray jsonArray) {
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                Log.e("TWEET PARSE ERROR", e.getMessage());
                continue;
            }

            Tweet tweet = new Tweet();
            tweet = createTweetfromJson(tweetJson, tweet);
            if (tweet != null) {
                tweets.add(tweet);
            }

            if (tweet.getRetweet() != null) {
                if(!retweets.contains(tweet.getRetweet())) {
                    retweets.add(tweet.getRetweet());
                }
            }
        }

        if (retweets != null) {
            retweetDAO.saveRetweetList(retweets);
        }

        if (tweets != null) {
            tweetDAO.saveTweetList(tweets);
        }
    }

    private Tweet createTweetfromJson(JSONObject jsonObject, Tweet tweet) {
        try {
            tweet.setBody(jsonObject.getString("text"));
            tweet.setTweetId(jsonObject.getLong("id"));
            tweet.setFavorited(jsonObject.getBoolean("favorited"));
            tweet.setRetweeted(jsonObject.getBoolean("retweeted"));

            if (StringUtils.contains(tweet.getBody(), "RT @")) {
                Retweet retweet = new Retweet();
                retweet = createRetweetFromJson(jsonObject.getJSONObject("retweeted_status"), retweet);
                tweet.setRetweet(retweet);
            }
            tweet.setCreatedDate(JodaDateUtils.parseDateTime(jsonObject.getString("created_at")));
            tweet.setUser(UserUtil.queryOrCreateUser(jsonObject.getJSONObject("user")));
        } catch (JSONException e) {
            Log.e("ERROR", e.getMessage());
            return null;
        }
        return tweet;
    }

    private Retweet createRetweetFromJson(JSONObject jsonObject, Retweet retweet) {
        try {
            retweet.setBody(jsonObject.getString("text"));
            retweet.setTweetId(jsonObject.getLong("id"));
            retweet.setFavorited(jsonObject.getBoolean("favorited"));
            retweet.setRetweeted(jsonObject.getBoolean("retweeted"));
            retweet.setCreatedDate(JodaDateUtils.parseDateTime(jsonObject.getString("created_at")));
            retweet.setRetweetedUser(UserUtil.queryOrCreateRetweetedUser(jsonObject.getJSONObject("user")));
        } catch (JSONException e) {
            Log.e("ERROR", e.getMessage());
            return null;
        }
        return retweet;
    }


}
