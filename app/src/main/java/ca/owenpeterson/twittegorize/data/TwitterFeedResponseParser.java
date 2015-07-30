package ca.owenpeterson.twittegorize.data;

import android.os.AsyncTask;
import android.util.Log;

import com.activeandroid.query.Select;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ca.owenpeterson.twittegorize.listeners.FeedResponseParsed;
import ca.owenpeterson.twittegorize.models.Retweet;
import ca.owenpeterson.twittegorize.models.RetweetedUser;
import ca.owenpeterson.twittegorize.models.Tweet;
import ca.owenpeterson.twittegorize.models.User;
import ca.owenpeterson.twittegorize.utils.JodaDateUtils;

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
            tweet.setUser(queryOrCreateUser(jsonObject.getJSONObject("user")));
        } catch (JSONException e) {
            Log.e("ERROR", e.getMessage());
            return null;
        }
        return tweet;
    }

    //TODO move this
    private User queryOrCreateUser(JSONObject jsonObject) {
        User jsonUser = createUserFromJson(jsonObject);
        long userId = jsonUser.getUserId();

        //TODO: create a method in the TwitterUserManager class that handles this instead.
        User existingUser = new Select().from(User.class).where("userId = ?", userId).executeSingle();

        if (existingUser != null) {
            return existingUser;
        } else {
            jsonUser.save();
            return jsonUser;
        }
    }

    private User createUserFromJson(JSONObject json) {
        User u = new User();
        try {
            u.setName(json.getString("name"));
            u.setUserId(json.getLong("id"));
            u.setScreenName(json.getString("screen_name"));
            u.setProfileBgImageUrl(json.getString("profile_background_image_url"));
            u.setProfileImageUrl(json.getString("profile_image_url"));
            u.setNumTweets(json.getInt("statuses_count"));
            u.setFollowersCount(json.getInt("followers_count"));
            u.setFriendsCount(json.getInt("friends_count"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }

    private RetweetedUser createRetweetedUserFromJson(JSONObject json) {
        RetweetedUser retweetedUser = new RetweetedUser();
        try {
            retweetedUser.setName(json.getString("name"));
            retweetedUser.setUserId(json.getLong("id"));
            retweetedUser.setScreenName(json.getString("screen_name"));
            retweetedUser.setProfileImageUrl(json.getString("profile_image_url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retweetedUser;
    }

    private Retweet createRetweetFromJson(JSONObject jsonObject, Retweet retweet) {
        try {
            retweet.setBody(jsonObject.getString("text"));
            retweet.setTweetId(jsonObject.getLong("id"));
            retweet.setFavorited(jsonObject.getBoolean("favorited"));
            retweet.setRetweeted(jsonObject.getBoolean("retweeted"));
            retweet.setCreatedDate(JodaDateUtils.parseDateTime(jsonObject.getString("created_at")));
            retweet.setRetweetedUser(queryOrCreateRetweetedUser(jsonObject.getJSONObject("user")));
        } catch (JSONException e) {
            Log.e("ERROR", e.getMessage());
            return null;
        }
        return retweet;
    }

    //TODO move this
    private RetweetedUser queryOrCreateRetweetedUser(JSONObject jsonObject) {

        RetweetedUser jsonUser = createRetweetedUserFromJson(jsonObject);
        long userId = jsonUser.getUserId();

        //TODO: create a method in the TwitterUserManager class that handles this instead.
        RetweetedUser existingUser = new Select().from(RetweetedUser.class).where("userId = ?", userId).executeSingle();

        if (existingUser != null) {
            return existingUser;
        } else {
            jsonUser.save();
            return jsonUser;
        }
    }
}
